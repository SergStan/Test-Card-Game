package com.example.testcardgameapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testcardgameapplication.data.model.Round

@Dao
interface RoundsDao {

    @Query("SELECT * FROM round")
    fun getAll(): List<Round>

    @Query("SELECT * FROM round WHERE game = :id")
    fun getGame(id: Long): List<Round>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(round: Round)

    @Query("DELETE FROM round WHERE game = :id")
    fun delete(id: Long)

    @Query("DELETE FROM round")
    fun deleteAll()
}