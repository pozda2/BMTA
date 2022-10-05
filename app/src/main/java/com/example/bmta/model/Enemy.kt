package com.example.bmta.model

data class Enemy(
    override var name: String = "",
    override var position: Position,
    override var health: Double = 100.0,
    override var attack: Double = 1.0,
    override var defense: Double = 1.0,
    override var command: String = "utok"
) : Character ()