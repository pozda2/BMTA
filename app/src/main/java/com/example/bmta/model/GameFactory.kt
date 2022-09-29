package com.example.bmta.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameFactory (
    private val heroName : String
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Game::class.java)) {
            return Game(heroName) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}