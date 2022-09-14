package com.example.bmta.model

enum class Terrain (val description: String, val terrainChar: Char) {
    BORDER ("hranice", '#'),
    MEADOW ("louka", ' '),
    FOREST ("les", '|'),
    RIVER ("řeka", '*'),
    BRIDGE ("most", '=')
}