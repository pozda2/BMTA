package com.example.bmta.model

import com.example.bmta.R

enum class GameObject (val imgResource: Int, val command: String) {
    BORDER (R.drawable.ic_border, "nop"),
    MEADOW (R.drawable.ic_meadow, ""),
    FOREST (R.drawable.ic_forest, "kacej"),
    RIVER (R.drawable.ic_river, "nop"),
    BRIDGE (R.drawable.ic_bridge, ""),
    SKELETON(R.drawable.ic_skeleton, "zautoc"),
    TROLL(R.drawable.ic_troll, "zautoc"),
    NONE(R.drawable.ic_knight, "nop")
}