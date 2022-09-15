package com.example.bmta.model

data class Position (var x: Int, var y: Int) {
    fun isFree (gameObjects: ArrayList<Any>): Boolean {
        for (gameObject in gameObjects) {
            if (gameObject is GameObject ) {
                if (gameObject.position == this) {
                    return false
                }
            }
        }
        return true
    }
}