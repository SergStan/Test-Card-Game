@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.testcardgameapplication.presentation.game

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testcardgameapplication.data.repository.RoundRepository
import com.example.testcardgameapplication.domain.Game
import com.example.testcardgameapplication.presentation.game.GameViewModel
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class GameViewModelTest : TestCase() {

    @Rule
    @JvmField
    val testRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var repository: RoundRepository

    private lateinit var ioCoroutineDispatcher: CoroutineDispatcher
    private lateinit var mainCoroutineDispatcher: CoroutineDispatcher

    private lateinit var gameViewModel: GameViewModel

    @Before
    fun setup() {
        ioCoroutineDispatcher = UnconfinedTestDispatcher()
        mainCoroutineDispatcher = UnconfinedTestDispatcher()
        application = mock<Application>()
        repository = mock<RoundRepository>()
        gameViewModel = GameViewModel(
            application,
            repository,
            ioCoroutineDispatcher,
            mainCoroutineDispatcher
        )
    }

    @Test
    fun `test first new game`() {
        runTest {
            gameViewModel.game = null

            assertNull(gameViewModel.game)
            gameViewModel.newGame()
            assertNotNull(gameViewModel.game)
            verify(repository, never()).deleteGameRounds(any())
        }
    }

    @Test
    fun `test new game when last game is not finished`() {
        runTest {
            gameViewModel.game = null

            val game: Game = mock<Game>()
            gameViewModel.game = game
            `when`(game.deckHasCards).thenReturn(true)
            gameViewModel.newGame()
            verify(repository, times(1)).deleteGameRounds(any())
        }
    }

    @Test
    fun `test next step on game end`() {
        gameViewModel.game = null

        val game: Game = mock<Game>()
        gameViewModel.game = game
        `when`(game.playersHaveCards).thenReturn(false)
        gameViewModel.nextStep()
        verify(game, times(1)).gameResult()
        verify(game, never()).showTopCards()
        verify(game, never()).trickCards()
    }

    @Test
    fun `test next step on open card`() {
        gameViewModel.game = null

        val game: Game = mock<Game>()
        gameViewModel.game = game
        `when`(game.playersHaveCards).thenReturn(true)
        `when`(game.needsTrick).thenReturn(false)
        gameViewModel.nextStep()
        verify(game, never()).gameResult()
        verify(game, times(1)).showTopCards()
        verify(game, never()).trickCards()
        assertNotNull(game)

    }

    @Test
    fun `test next step on trick card`() {
        gameViewModel.game = null

        val game: Game = mock<Game>()
        gameViewModel.game = game
        `when`(game.playersHaveCards).thenReturn(true)
        `when`(game.needsTrick).thenReturn(true)
        gameViewModel.nextStep()
        verify(game, never()).gameResult()
        verify(game, never()).showTopCards()
        verify(game, times(1)).trickCards()
        assertNotNull(game)
    }
}
