package com.example.bmta.model

data class Troll (override var name: String = "Troll",
                  override var position: Position,
                  override var health: Float = 15.0F,
                  override var attack: Float = 3.0F,
                  override var defense: Float = 0.9F,
                  ) :
    Enemy(name, position, health, attack, defense)