package com.example.bmta.model

enum class Direction (val relativeY : Int, val relativeX : Int, val description: String, val command: String ){
    NORTH (-1, 0, "sever", "sever"),
    SOUTH (1, 0, "jih", "jih"),
    EAST (0, 1, "východ", "vychod"),
    WEST (0, -1, "západ", "zapad"),
    NOMOVE (0, 0, "nop", "nop")
}