package com.example.testcardgameapplication.data.repository

import com.example.testcardgameapplication.data.dao.RoundsDao
import com.example.testcardgameapplication.data.model.RoundsResult
import com.example.testcardgameapplication.data.model.Round
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GameRepository(
    private val roundsDao: RoundsDao,
    private val ioCoroutineDispatcher: CoroutineDispatcher,
) : RoundRepository {
    override suspend fun getAllRounds(): RoundsResult {
        return withContext(ioCoroutineDispatcher) {
            try {
                val result = roundsDao.getAll()
                RoundsResult.Content(result)
            } catch (throwable: Throwable) {
                RoundsResult.Error(throwable)
            }
        }
    }

    override suspend fun getAllGameRounds(id: Long): RoundsResult {
        return withContext(ioCoroutineDispatcher) {
            try {
                val result = roundsDao.getGame(id)
                RoundsResult.Content(result)
            } catch (throwable: Throwable) {
                RoundsResult.Error(throwable)
            }
        }
    }

    override suspend fun insertRound(round: Round): Boolean {
        roundsDao.insert(round)
        return true
    }

    override suspend fun deleteGameRounds(id: Long): Boolean {
        return try {
            roundsDao.delete(id)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun deleteAllRounds(): Boolean {
        return try {
            roundsDao.deleteAll()
            true
        } catch (ex: Exception) {
            false
        }
    }
}