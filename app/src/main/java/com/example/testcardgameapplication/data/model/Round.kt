package com.example.testcardgameapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Round (

    val game: Long? = null,
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    var winner: String? = null,
    var trick: String? = null)