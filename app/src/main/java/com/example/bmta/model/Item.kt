package com.example.bmta.model

data class Item (
    override var name: String = "Item",
    override var position: Position,
    var pickedUp : Boolean = false,
    val health: Float = 0.0F,
    val attack: Float= 0.0F,
    val defense : Float= 0.0F,
    val healing : Float= 0.0F,
    override var command: String = name.lowercase()
) : GameObject()