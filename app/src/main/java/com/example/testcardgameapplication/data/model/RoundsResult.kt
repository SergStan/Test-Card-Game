package com.example.testcardgameapplication.data.model

sealed class RoundsResult {
    data class Content(val rounds: List<Round>) : RoundsResult()
    data class Error(val throwable: Throwable) : RoundsResult()
}
