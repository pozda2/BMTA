package com.example.bmta.model

enum class Direction (val relativeY : Int, val relativeX : Int, val description: String, val command: String ){
    NORTH (-1, 0, "sever", "s"),
    SOUTH (1, 0, "jih", "j"),
    EAST (0, 1, "východ", "v"),
    WEST (0, -1, "západ", "z"),
    NORTHEAST (-1, 1, "severovýchod", "sv"),
    SOUTHEAST (1, 1, "jihovýchod", "jv"),
    NORTHWEST (-1, -1, "severozápad", "sz"),
    SOUTHWEST (1, -1, "jihozápad", "jz"),
    NOMOVE (0, 0, "nop", "nop")
}