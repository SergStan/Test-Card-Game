package com.example.testcardgameapplication.data.database

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.testcardgameapplication.data.dao.RoundsDao
import com.example.testcardgameapplication.data.model.Round

@Database(entities = [Round::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun roundsDao(): RoundsDao
}