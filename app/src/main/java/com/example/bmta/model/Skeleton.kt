package com.example.bmta.model

data class Skeleton (override var name: String = "Skeleton",
                     override var position: Position,
                     override var health: Float = 10.0F,
                     override var attack: Float = 5.0F,
                     override var defense: Float = 0.5F, ) :
    Enemy(name, position, health, attack, defense)