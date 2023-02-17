package com.example.testcardgameapplication.presentation.game

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.testcardgameapplication.R
import com.example.testcardgameapplication.databinding.ActivityGameBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.appcompat.app.AlertDialog
import com.example.testcardgameapplication.presentation.history.HistoryActivity

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private val viewModel: GameViewModel by viewModel()

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        with(binding) {
            startHistory.setOnClickListener {
                val intent = HistoryActivity.newIntent(this@GameActivity)
                startActivity(intent)
            }
            startButton.setOnClickListener {
                alert()
            }
            openCard.setOnClickListener {
                viewModel.nextStep()
            }
            viewModel.gameState.observe(this@GameActivity) { state ->
                progressBar.visibility = View.GONE
                when (state) {
                    is GameState.Load -> {
                        progressBar.visibility = View.VISIBLE
                        shortToast(getString(state.processDescription))
                        deckSizeTv.text = state.cardsInDeck.toString()
                        emptyValues()
                        closeSuit()
                        enableElements(false)
                    }
                    is GameState.SuitsValue -> {
                        progressBar.visibility = View.VISIBLE
                        state.listValues.map { suit ->
                            when (suit.value) {
                                4 -> suitPriorityFirst.setImageDrawable(getDrawable(suit.iconIdRes))
                                3 -> suitPrioritySecond.setImageDrawable(getDrawable(suit.iconIdRes))
                                2 -> suitPriorityThird.setImageDrawable(getDrawable(suit.iconIdRes))
                                1 -> suitPriorityForth.setImageDrawable(getDrawable(suit.iconIdRes))
                            }
                        }
                        shortToast(getString(state.processDescription))
                    }
                    is GameState.DealCards -> {
                        enableElements(state.cardsInDeck == EMPTY_DECK_SIZE)
                        @Suppress("UNUSED_EXPRESSION")
                        deckSizeTv.text = state.cardsInDeck.toString()
                        playerGameDeckSize.text = state.playerStartDeck.toString()
                        androidGameDeckSize.text = state.androidStartDeck.toString()
                        closeSuit()
                    }
                    is GameState.OpenCards -> {
                        openSuit(state)
                        player.setBackgroundColor(getColor(state.playerCardColorRes))
                        android.setBackgroundColor(getColor(state.androidCardColorRes))
                    }

                    is GameState.Trick -> {
                        playerGameDeckSize.text = state.playerStartDeck.toString()
                        androidGameDeckSize.text = state.androidStartDeck.toString()
                        playerResultDeckSize.text = state.playerResultDeck.toString()
                        androidResultDeckSize.text = state.androidResultDeck.toString()
                        closeSuit()
                    }
                    is GameState.Result -> {
                        openCard.isEnabled = false
                        longToast("Game: ${state.gameName}\nGame Score: ${state.gameScore}\n${state.winnerName}\n${state.looserName}")
                    }
                    is GameState.Error -> {
                        shortToast(getString(state.errorMessage))
                    }
                    else -> {
                        shortToast(getString(R.string.error_message))
                    }
                }
            }
        }
    }

    private fun emptyValues() = with(binding) {
        playerGameDeckSize.text = EMPTY_DECK_SIZE.toString()
        androidGameDeckSize.text = EMPTY_DECK_SIZE.toString()
        playerResultDeckSize.text = EMPTY_DECK_SIZE.toString()
        androidResultDeckSize.text = EMPTY_DECK_SIZE.toString()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun closeSuit() = with(binding) {
        playerGameDeckSize.setBackgroundResource(R.drawable.bg_card_closed)
        androidGameDeckSize.setBackgroundResource(R.drawable.bg_card_closed)
        playerOpenCardGroup.visibility = View.GONE
        androidOpenCardGroup.visibility = View.GONE
        player.setBackgroundColor(getColor(R.color.white))
        android.setBackgroundColor(getColor(R.color.white))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun openSuit(state: GameState.OpenCards) = with(binding) {
        androidOpenCardRang.text = state.androidTopCardInDeck.rankShort
        androidOpenCardRang.setTextColor(getColor(state.androidTopCardInDeck.suitColor))
        androidOpenCardSuit.setImageDrawable(getDrawable(state.androidTopCardInDeck.suitIcon))

        playerOpenCardRang.text = state.playerTopCardInDeck.rankShort
        playerOpenCardRang.setTextColor(getColor(state.playerTopCardInDeck.suitColor))
        playerOpenCardSuit.setImageDrawable(getDrawable(state.playerTopCardInDeck.suitIcon))

        playerGameDeckSize.text = EMPTY
        androidGameDeckSize.text = EMPTY

        playerGameDeckSize.setBackgroundResource(R.drawable.bg_card_opened)
        androidGameDeckSize.setBackgroundResource(R.drawable.bg_card_opened)

        playerOpenCardGroup.visibility = View.VISIBLE
        androidOpenCardGroup.visibility = View.VISIBLE
    }

    private fun alert() {
        val builder = AlertDialog.Builder(this)
        with(builder)
        {
            setTitle(getString(R.string.dialog_new_game_title))
            setMessage(getString(R.string.dialog_new_game_msg))
            setPositiveButton(getString(R.string.dialog_new_game_positive_btn_title)) { _, _ -> viewModel.newGame() }
            setNegativeButton(getString(R.string.dialog_new_game_cancel_btn_title)) { _, _ -> }
            show()
        }
    }

    private fun shortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun longToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun enableElements(enable: Boolean) {
        with(binding) {
            startButton.isEnabled = enable
            openCard.isEnabled = enable
        }
    }

    companion object {
        private const val EMPTY: String = ""
        private const val EMPTY_DECK_SIZE: Int = 0
    }
}