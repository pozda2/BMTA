package com.example.bmta.model

data class Position (var x: Int, var y: Int) {
    fun isFree (gameObjects: ArrayList<GameObject>): Boolean {
        for (gameObject in gameObjects) {
            if (gameObject.position == this) {
                return false
            }
        }
        return true
    }
}