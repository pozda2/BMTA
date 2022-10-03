package com.example.bmta.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScore(score: Score)

    @Query("SELECT * FROM score ORDER BY score")
    fun getScores(): Flow<List<Score>>

}