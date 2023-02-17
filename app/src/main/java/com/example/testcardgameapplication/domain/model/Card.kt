package com.example.testcardgameapplication.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.example.testcardgameapplication.R

enum class Card(
    val value: Int,
    val suit: String,
    val rank: String,
    val rankShort: String,
    @ColorRes val suitColor: Int,
    @DrawableRes val suitIcon: Int
) {
    SPADES_ACE(13, "Spades", "Ace", "A", R.color.black, R.drawable.ic_spades),
    SPADES_KING(12, "Spades", "King", "K", R.color.black, R.drawable.ic_spades),
    SPADES_QUEEN(11, "Spades", "Queen", "Q", R.color.black, R.drawable.ic_spades),
    SPADES_JACK(10, "Spades", "Jack", "J", R.color.black, R.drawable.ic_spades),
    SPADES_TEN(9, "Spades", "Ten", "10", R.color.black, R.drawable.ic_spades),
    SPADES_NINE(8, "Spades", "Nine", "9", R.color.black, R.drawable.ic_spades),
    SPADES_EIGHT(7, "Spades", "Eight", "8", R.color.black, R.drawable.ic_spades),
    SPADES_SEVEN(6, "Spades", "Seven", "7", R.color.black, R.drawable.ic_spades),
    SPADES_SIX(5, "Spades", "Six", "6", R.color.black, R.drawable.ic_spades),
    SPADES_FIVE(4, "Spades", "Five", "5", R.color.black, R.drawable.ic_spades),
    SPADES_FOUR(3, "Spades", "Four", "4", R.color.black, R.drawable.ic_spades),
    SPADES_THREE(2, "Spades", "Three", "3", R.color.black, R.drawable.ic_spades),
    SPADES_TWO(1, "Spades", "Two", "2", R.color.black, R.drawable.ic_spades),

    CLUBS_ACE(13, "Clubs", "Ace", "A", R.color.black, R.drawable.ic_clubs),
    CLUBS_KING(12, "Clubs", "King", "K", R.color.black, R.drawable.ic_clubs),
    CLUBS_QUEEN(11, "Clubs", "Queen", "Q", R.color.black, R.drawable.ic_clubs),
    CLUBS_JACK(10, "Clubs", "Jack", "J", R.color.black, R.drawable.ic_clubs),
    CLUBS_TEN(9, "Clubs", "Ten", "10", R.color.black, R.drawable.ic_clubs),
    CLUBS_NINE(8, "Clubs", "Nine", "9", R.color.black, R.drawable.ic_clubs),
    CLUBS_EIGHT(7, "Clubs", "Eight", "8", R.color.black, R.drawable.ic_clubs),
    CLUBS_SEVEN(6, "Clubs", "Seven", "7", R.color.black, R.drawable.ic_clubs),
    CLUBS_SIX(5, "Clubs", "Six", "6", R.color.black, R.drawable.ic_clubs),
    CLUBS_FIVE(4, "Clubs", "Five", "5", R.color.black, R.drawable.ic_clubs),
    CLUBS_FOUR(3, "Clubs", "Four", "4", R.color.black, R.drawable.ic_clubs),
    CLUBS_THREE(2, "Clubs", "Three", "3", R.color.black, R.drawable.ic_clubs),
    CLUBS_TWO(1, "Clubs", "Two", "2", R.color.black, R.drawable.ic_clubs),

    DIAMONDS_ACE(13, "Diamonds", "Ace", "A", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_KING(12, "Diamonds", "King", "K", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_QUEEN(11, "Diamonds", "Queen", "Q", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_JACK(10, "Diamonds", "Jack", "J", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_TEN(9, "Diamonds", "Ten", "10", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_NINE(8, "Diamonds", "Nine", "9", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_EIGHT(7, "Diamonds", "Eight", "8", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_SEVEN(6, "Diamonds", "Seven", "7", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_SIX(5, "Diamonds", "Six", "6", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_FIVE(4, "Diamonds", "Five", "5", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_FOUR(3, "Diamonds", "Four", "4", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_THREE(2, "Diamonds", "Three", "3", R.color.red, R.drawable.ic_diamonds),
    DIAMONDS_TWO(1, "Diamonds", "Two", "2", R.color.red, R.drawable.ic_diamonds),

    HEARTS_ACE(13, "Hearts", "Ace", "A", R.color.red, R.drawable.ic_hearts),
    HEARTS_KING(12, "Hearts", "King", "K", R.color.red, R.drawable.ic_hearts),
    HEARTS_QUEEN(11, "Hearts", "Queen", "Q", R.color.red, R.drawable.ic_hearts),
    HEARTS_JACK(10, "Hearts", "Jack", "J", R.color.red, R.drawable.ic_hearts),
    HEARTS_TEN(9, "Hearts", "Ten", "10", R.color.red, R.drawable.ic_hearts),
    HEARTS_NINE(8, "Hearts", "Nine", "9", R.color.red, R.drawable.ic_hearts),
    HEARTS_EIGHT(7, "Hearts", "Eight", "8", R.color.red, R.drawable.ic_hearts),
    HEARTS_SEVEN(6, "Hearts", "Seven", "7", R.color.red, R.drawable.ic_hearts),
    HEARTS_SIX(5, "Hearts", "Six", "6", R.color.red, R.drawable.ic_hearts),
    HEARTS_FIVE(4, "Hearts", "Five", "5", R.color.red, R.drawable.ic_hearts),
    HEARTS_FOUR(3, "Hearts", "Four", "4", R.color.red, R.drawable.ic_hearts),
    HEARTS_THREE(2, "Hearts", "Three", "3", R.color.red, R.drawable.ic_hearts),
    HEARTS_TWO(1, "Hearts", "Two", "2", R.color.red, R.drawable.ic_hearts),

}

fun Card.name() = "${this.rankShort} ${this.suit}"
