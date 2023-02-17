package com.example.testcardgameapplication.presentation.history

data class HeaderState(
    val name: String,
    val score: String,
    val winner: String
): ListItem(TYPE_HEADER)
