package com.example.testcardgameapplication.domain

import android.util.Log
import com.example.testcardgameapplication.ANDROID
import com.example.testcardgameapplication.PLAYER
import com.example.testcardgameapplication.R
import com.example.testcardgameapplication.data.repository.RoundRepository
import com.example.testcardgameapplication.data.model.Round
import com.example.testcardgameapplication.domain.model.*
import com.example.testcardgameapplication.presentation.game.GameState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.ThreadLocalRandom

class Game(
    var id: Long,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val repository: RoundRepository
) {
    private lateinit var deck: Deck
    private lateinit var suitValueList: List<Suit>
    private val dealer: Dealer = Dealer()
    private val player: Player = Player(PLAYER)
    private var playerTopCardInDeck: Card? = null
    private val android: Player = Player(ANDROID)
    private var androidTopCardInDeck: Card? = null
    private var currentRound: Round? = null

    val deckHasCards: Boolean
        get() = deck.cards.size > 0

    val playersHaveCards: Boolean
        get() = player.hasCard && android.hasCard

    val needsTrick: Boolean
        get() = playerTopCardInDeck != null && androidTopCardInDeck != null

    suspend fun suitPrioritySelection(): GameState =
        withContext(coroutineDispatcher) {
            val priorityList: MutableList<Int> = mutableListOf()
            for (i in Suit.values().indices) {
                priorityList.add(i + 1)
            }

            suitValueList = mutableListOf()
            suitValueList = Suit.values().map {
                val startIndex = 0
                val endIndex = priorityList.size - 1
                val randomIndex =
                    if (startIndex == endIndex) {
                        startIndex
                    } else {
                        randomInt(startIndex, endIndex)
                    }
                it.value = priorityList[randomIndex]
                priorityList.removeAt(randomIndex)
                it
            }
            Log.d(
                "TAAG",
                "Suit Priority List size: ${suitValueList.size} \n Suit Priority List: ${suitValueList.map { it.name + " pr: " + it.value }}"
            )
            return@withContext GameState.SuitsValue(
                suitValueList,
                R.string.progress_suit_priority_selection
            )
        }

    suspend fun shuffleDeck(): GameState = withContext(coroutineDispatcher) {
        deck = dealer.openDeck()
        Log.d(
            "TAAG",
            "deck size before shuffle: ${deck.cards.size} \ndeck before shuffle: ${deck.cards}"
        )
        dealer.shuffleDeck(deck)
        Log.d(
            "TAAG",
            "shuffled deck size: ${deck.cards.size} \nshuffled deck : ${deck.cards}"
        )
        return@withContext GameState.Load(R.string.progress_deck_cards_shuffling)
    }

    fun dealCards(): GameState {

        val deckCardSetSize = deck.cards.size
        val playerStartDeckCardSetSize = player.startDeck.cards.size
        val androidStartDeckCardSetSize = android.startDeck.cards.size

        val card = dealer.dealСard(deck) ?: return GameState.DealCards(
            cardsInDeck = 0,
            playerStartDeck = playerStartDeckCardSetSize,
            androidStartDeck = androidStartDeckCardSetSize,
        )
        if (deckCardSetSize % 2 == 1) {
            player.startDeck.cards.add(playerStartDeckCardSetSize, card)
        } else {
            android.startDeck.cards.add(androidStartDeckCardSetSize, card)
            Log.d(
                "TAAG",
                "user start deck size: ${player.startDeck.cards.size} \nuser start deck : ${player.startDeck.cards} \nrobot start deck size: ${android.startDeck.cards.size}\nrobot start deck : ${android.startDeck.cards}"
            )
        }
        return GameState.DealCards(
            cardsInDeck = deck.cards.size,
            playerStartDeck = player.startDeck.cards.size,
            androidStartDeck = android.startDeck.cards.size,
        )
    }

    fun showTopCards(): GameState {
        playerTopCardInDeck = player.getCardFromTop()
        androidTopCardInDeck = android.getCardFromTop()
        Log.d("TAAG", "show Top Cards : $playerTopCardInDeck $androidTopCardInDeck")
        return playerTopCardInDeck?.let { playerCard ->
            androidTopCardInDeck?.let { androidCard ->
                compareCards()?.let { resources ->
                    GameState.OpenCards(
                        playerTopCardInDeck = playerCard,
                        androidTopCardInDeck = androidCard,
                        playerCardColorRes = resources.first,
                        androidCardColorRes = resources.second
                    )
                }
            }
        } ?: GameState.Error(
            R.string.error_message
        )
    }

    private fun compareCards(): Pair<Int, Int>? {
        playerTopCardInDeck?.let { playerCard ->
            androidTopCardInDeck?.let { autoCard ->
                when {
                    (playerCard.value > autoCard.value) -> {
                        currentRound = Round(
                            game = id,
                            winner = player.name,
                            trick = "${playerCard.name()} : ${autoCard.name()}"
                        )
                    }
                    (playerCard.value < autoCard.value) -> {
                        currentRound = Round(
                            game = id,
                            winner = android.name,
                            trick = "${playerCard.name()} : ${autoCard.name()}"
                        )
                    }
                    else -> {
                        try {
                            val playerTopCardInDeckSuitValue = suitValueList.first {
                                it.stringName == playerCard.suit
                            }.value

                            val androidTopCardInDeckSuitValue = suitValueList.first {
                                it.stringName == autoCard.suit
                            }.value

                            currentRound = when {
                                (playerTopCardInDeckSuitValue > androidTopCardInDeckSuitValue) -> {
                                    Round(
                                        game = id,
                                        winner = player.name,
                                        trick = "${playerCard.name()} : ${autoCard.name()}"
                                    )
                                }
                                (playerTopCardInDeckSuitValue < androidTopCardInDeckSuitValue) -> {
                                    Round(
                                        game = id,
                                        winner = android.name,
                                        trick = "${playerCard.name()} : ${autoCard.name()}"
                                    )
                                }
                                else -> return null
                            }
                        } catch (throwable: Throwable) {
                            return null
                        }
                    }
                }
                Log.d("TAAG", "compare Cards ROUND: $currentRound")
            }
        }
        val isPlayerWinner: Boolean =
            currentRound?.winner?.equals(player.name) ?: false
        val isAndroidWinner: Boolean =
            currentRound?.winner?.equals(android.name) ?: false

        val playerCardColorRes: Int = if (isPlayerWinner) {
            R.color.green
        } else {
            R.color.white
        }

        val androidCardColorRes: Int = if (isAndroidWinner) {
            R.color.green
        } else {
            R.color.white
        }
        return Pair(playerCardColorRes, androidCardColorRes)
    }

    fun trickCards(): GameState {
        currentRound?.let { round ->
            playerTopCardInDeck?.let { playerCard ->
                androidTopCardInDeck?.let { autoCard ->
                    when (round.winner) {
                        player.name -> {
                            player.addTrick(Pair(playerCard, autoCard))
                        }
                        android.name -> {
                            android.addTrick(Pair(playerCard, autoCard))
                        }
                    }
                    Log.d(
                        "TAAG",
                        "result Cards player: ${player.resultDeck.cards}\nresult Cards android: ${android.resultDeck.cards}"
                    )
                    player.removeCardFromTop()
                    android.removeCardFromTop()
                    Log.d(
                        "TAAG",
                        "start Cards player: ${player.startDeck.cards}\nstart Cards android: ${android.startDeck.cards}"
                    )
                    playerTopCardInDeck = null
                    androidTopCardInDeck = null

                    runBlocking {
                        withContext(coroutineDispatcher) {
                            currentRound?.let {
                                repository.insertRound(it)
                            }
                        }
                    }
                    currentRound = null

                }
            }
        }
        return GameState.Trick(
            playerStartDeck = player.startDeck.cards.size,
            playerResultDeck = player.resultDeck.cards.size,
            androidStartDeck = android.startDeck.cards.size,
            androidResultDeck = android.resultDeck.cards.size
        )
    }

    fun gameResult(): GameState {
        val playerResultDeckCardSetSize: Int = player.resultDeck.cards.size
        val androidResultDeckCardSetSize: Int = android.resultDeck.cards.size
        val gameName: String = id.toString()
        val gameScore: String =
            "$playerResultDeckCardSetSize : $androidResultDeckCardSetSize"
        val winnerName: String = "Winner: ${
            when {
                (playerResultDeckCardSetSize > androidResultDeckCardSetSize) -> player.name

                (playerResultDeckCardSetSize < androidResultDeckCardSetSize) -> android.name
                else -> "none"
            }
        }"
        val looserName: String = "Looser: ${
            when {
                (playerResultDeckCardSetSize > androidResultDeckCardSetSize) -> android.name

                (playerResultDeckCardSetSize < androidResultDeckCardSetSize) -> player.name
                else -> "none"
            }
        }"
        return GameState.Result(
            gameName = gameName,
            gameScore = gameScore,
            winnerName = winnerName,
            looserName = looserName,
        )
    }

    companion object {
        fun randomInt(min: Int, max: Int): Int =
            ThreadLocalRandom.current().nextInt(max - min + 1) + min
    }
}