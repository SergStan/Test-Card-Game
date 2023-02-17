package com.example.testcardgameapplication

import android.content.Context
import androidx.room.Room
import com.example.testcardgameapplication.data.repository.RoundRepository
import com.example.testcardgameapplication.data.dao.RoundsDao
import com.example.testcardgameapplication.data.database.GameDatabase
import com.example.testcardgameapplication.data.repository.GameRepository
import com.example.testcardgameapplication.presentation.game.GameViewModel
import com.example.testcardgameapplication.presentation.history.HistoryViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val GAME_DATA_BASE = "game-database"

const val PLAYER = "player"
const val ANDROID = "android"
const val DRAW = "draw"

val gameModule = module {

    single<Context> { androidApplication() }
    single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
    single<CoroutineDispatcher>(named("main")) { Dispatchers.Main }
    single<GameDatabase> {
        Room.databaseBuilder(
            get(),
            GameDatabase::class.java,
            GAME_DATA_BASE
        ).build()
    }
    single<RoundsDao> {
        val database = get<GameDatabase>()
        database.roundsDao()
    }
    single<RoundRepository> { GameRepository(get(), get(named("io"))) }
    viewModel<GameViewModel> {
        GameViewModel(
            get(),
            get(),
            get(named("io")),
            get(named("main"))
        )
    }
    viewModel<HistoryViewModel> {
        HistoryViewModel(
            get(),
            get(),
            get(named("io")),
            get(named("main"))
        )
    }
}
