package com.example.testcardgameapplication.data.repository

import com.example.testcardgameapplication.data.model.RoundsResult
import com.example.testcardgameapplication.data.model.Round

interface RoundRepository {
   suspend fun getAllRounds(): RoundsResult
   suspend fun getAllGameRounds(id: Long): RoundsResult
   suspend fun insertRound(round:Round): Boolean
   suspend fun deleteGameRounds(id: Long): Boolean
   suspend fun deleteAllRounds(): Boolean
}