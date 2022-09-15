package com.example.bmta.model

import com.example.bmta.R

enum class Terrain(val description: String, val terrainChar: Char, val command: String, val imgResource: Int) {
    BORDER ("hranice", '#', "nop", R.drawable.ic_border),
    MEADOW ("louka", ' ', "", R.drawable.ic_meadow),
    FOREST ("les", '|', "kacej", R.drawable.ic_forest),
    RIVER ("řeka", '*', "nop", R.drawable.ic_river),
    BRIDGE ("most", '=', "", R.drawable.ic_bridge)
}