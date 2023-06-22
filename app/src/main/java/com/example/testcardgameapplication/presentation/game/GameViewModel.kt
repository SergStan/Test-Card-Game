package com.example.testcardgameapplication.presentation.game

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testcardgameapplication.data.repository.RoundRepository
import com.example.testcardgameapplication.domain.Game
import kotlinx.coroutines.*

class GameViewModel(
    application: Application,
    private val repository: RoundRepository,
    private val ioCoroutineDispatcher: CoroutineDispatcher,
    private val mainCoroutineDispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var game: Game? = null

    private val _gameState: MutableLiveData<GameState> = MutableLiveData<GameState>()
    val gameState: LiveData<GameState>
        get() = _gameState


    fun newGame() {
        viewModelScope.launch(mainCoroutineDispatcher) {
            val job = launch(ioCoroutineDispatcher) {
                game?.let {
                    if (it.playersHaveCards) {
                        repository.deleteGameRounds(it.id)
                    }
                }
            }
            job.join()
            game = Game(
                id = System.currentTimeMillis(),
                coroutineDispatcher = ioCoroutineDispatcher,
                repository = repository
            )
            startGame()
        }
    }

    private fun startGame() {
        viewModelScope.launch(mainCoroutineDispatcher) {
            game?.let {
                val suit: Job =
                    launch {
                        _gameState.postValue(it.suitPrioritySelection())
                    }
                val shuffle: Job =
                    launch {
                        _gameState.postValue(it.shuffleDeck())
                    }
                suit.join()
                shuffle.join()
                delay(JUST_SEE_TOASTS_DELAY)

                while (it.deckHasCards) {
                    _gameState.postValue(it.dealCards())
                    delay(DEAL_CARD_DELAY)
                }
            }
        }
    }

    fun nextStep() {
        game?.let {
            if (!it.playersHaveCards) {
                _gameState.postValue(it.gameResult())
                game = null
                return
            }
            if (!it.needsTrick) {
                _gameState.postValue(it.showTopCards())
            } else {
                _gameState.postValue(it.trickCards())
            }
        }
    }

    companion object{
        private const val DEAL_CARD_DELAY = 150L
        private const val JUST_SEE_TOASTS_DELAY = 3000L
    }
}

