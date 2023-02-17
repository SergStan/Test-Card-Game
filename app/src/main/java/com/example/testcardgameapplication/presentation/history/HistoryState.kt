package com.example.testcardgameapplication.presentation.history

sealed class HistoryState {
    object Loading : HistoryState()
    data class Content(val history: List<ListItem>): HistoryState()
    data class Error(val throwable: Throwable): HistoryState()
}
