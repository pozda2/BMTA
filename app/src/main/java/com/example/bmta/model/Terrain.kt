package com.example.bmta.model

enum class Terrain(val description: String, val terrainChar: Char, val command: String) {
    BORDER ("hranice", '#', "nop"),
    MEADOW ("louka", ' ', ""),
    FOREST ("les", '|', "kacej"),
    RIVER ("řeka", '*', "nop"),
    BRIDGE ("most", '=', "")
}