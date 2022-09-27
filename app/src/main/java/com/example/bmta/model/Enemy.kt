package com.example.bmta.model

data class Enemy(
    override var name: String = "",
    override var position: Position,
    override var health: Float = 100.0F,
    override var attack: Float = 1.0F,
    override var defense: Float = 1.0F,
    override var command: String,
    override var imgResource: Int
) : Character ()