package com.example.testcardgameapplication.presentation.history

open class ListItem(
    val type: Int
) {
    companion object {
        const val TYPE_ROW = 0
        const val TYPE_HEADER = 1
    }
}