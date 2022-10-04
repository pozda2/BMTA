package com.example.bmta.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "score")
data class Score(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")        val id : Int = 0,
    @ColumnInfo(name = "dateAdded") val dateAdded: Date,
    @ColumnInfo(name = "name")      val name: String,
    @ColumnInfo(name = "score")     val score: Int
)