package com.example.bmta.model

data class Position (var x: Int, var y: Int) {
    fun isFree (characters: ArrayList<Any>): Boolean {
        for (character in characters) {
            if (character is Character) {
                if (character.position == this) {
                    return false
                }
            }
        }
        return true
    }
}