package com.example.testcardgameapplication.domain.model

import androidx.annotation.DrawableRes
import com.example.testcardgameapplication.R

enum class Suit(
    val stringName: String,
    var value: Int = -1,
    @DrawableRes
    val iconIdRes: Int
) {
    SPADES(stringName = "Spades", iconIdRes = R.drawable.ic_spades),
    CLUBS(stringName = "Clubs", iconIdRes = R.drawable.ic_clubs),
    DIAMONDS(stringName = "Diamonds", iconIdRes = R.drawable.ic_diamonds),
    HEARTS(stringName = "Hearts", iconIdRes = R.drawable.ic_hearts)
}