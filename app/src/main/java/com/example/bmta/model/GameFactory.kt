package com.example.bmta.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bmta.database.ScoreDao

class GameFactory (
    private val heroName : String,
    private val settingsJson : String,
    private val scoreDatabase : ScoreDao
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Game::class.java)) {
            return Game(heroName, settingsJson, scoreDatabase) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}