package com.example.testcardgameapplication.presentation.history

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testcardgameapplication.ANDROID
import com.example.testcardgameapplication.DRAW
import com.example.testcardgameapplication.PLAYER
import com.example.testcardgameapplication.data.model.Round
import com.example.testcardgameapplication.data.repository.RoundRepository
import com.example.testcardgameapplication.data.model.RoundsResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewModel(
    application: Application,
    private val repository: RoundRepository,
    private val ioCoroutineDispatcher: CoroutineDispatcher,
    private val mainCoroutineDispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {

    private val _result = MutableLiveData<HistoryState>()
    val result: LiveData<HistoryState> = _result

    fun loadHistory() {
        _result.value = HistoryState.Loading
        viewModelScope.launch(mainCoroutineDispatcher) {
            val result = repository.getAllRounds()

            _result.value = when (result) {
                is RoundsResult.Content -> {
                    val groupedMapMap: Map<String, List<Round>> = result.rounds.groupBy {
                        it.game.toString()
                    }
                    Log.d("TAAAG", "repository.getAllRounds() ${groupedMapMap.size}")
                    val consolidatedList = mutableListOf<ListItem>()
                    for (game: String in groupedMapMap.keys) {
                        val groupItems: List<Round> = groupedMapMap[game]!!
                        if (groupItems.size != FULL_GAME_TRICKS) continue
                        if (groupItems.isNotEmpty()) {
                            consolidatedList.add(prepareHeaderState(groupItems))
                            groupItems.forEach { round ->
                                consolidatedList.add(RowState(round.trick!!))
                            }
                        }
                    }
                    HistoryState.Content(consolidatedList)
                }

                is RoundsResult.Error -> {
                    HistoryState.Error(result.throwable)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun prepareHeaderState(list: List<Round>): ListItem {
        fun getDateTime(timestamp: Long): String? {
            return try {
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val netDate = Date(timestamp)
                simpleDateFormat.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }

        val name = list.first().game?.let { getDateTime(it) } ?: list.first().game.toString()
        val listSize = list.size
        val playerWinList = list.filter {
            it.winner.equals(PLAYER)
        }
        val playerWinSize = playerWinList.size
        val androidWinSize = listSize - playerWinSize
        val score = "$androidWinSize : $playerWinSize"
        val winner = when {
            (androidWinSize > playerWinSize) -> ANDROID
            (androidWinSize < playerWinSize) -> PLAYER
            else -> DRAW
        }
        return HeaderState(name, score, winner)
    }

    fun clearHistory() {
        viewModelScope.launch(mainCoroutineDispatcher) {
            val def = async(ioCoroutineDispatcher) {
                repository.deleteAllRounds()
            }
            if (def.await()) {
                loadHistory()
            } else {
                Log.d("TAAAG", "history was not deleted")
            }
        }
    }

    companion object {
        private const val FULL_GAME_TRICKS = 26
    }
}