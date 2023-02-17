package com.example.testcardgameapplication.presentation.game

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.testcardgameapplication.domain.model.Card
import com.example.testcardgameapplication.domain.model.Suit

sealed class GameState {

    class Load(
        @StringRes
        val processDescription: Int,
        val cardsInDeck: Int = 0
        ) : GameState()

    class SuitsValue(
        val listValues: List<Suit>,
        @StringRes
        val processDescription: Int
    ) : GameState()

       class DealCards(
        val cardsInDeck: Int = 0,
        val playerStartDeck: Int = 0,
        val androidStartDeck: Int = 0,
    ) : GameState()

    class OpenCards(
        val playerTopCardInDeck: Card,
        val androidTopCardInDeck: Card,
        @ColorRes
        val playerCardColorRes: Int,
        @ColorRes
        val androidCardColorRes: Int
    ) : GameState()

    class Trick(
        val playerStartDeck: Int = 0,
        val playerResultDeck: Int = 0,
        val androidStartDeck: Int = 0,
        val androidResultDeck: Int = 0
    ) : GameState()


    class Result(
        val gameName: String,
        val gameScore: String,
        val winnerName: String,
        val looserName: String
    ) : GameState()

    class Error(
        @StringRes
        val errorMessage: Int
    ) : GameState()
}
