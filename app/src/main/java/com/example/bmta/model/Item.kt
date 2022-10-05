package com.example.bmta.model

data class Item (
    override var name: String = "Item",
    override var position: Position,
    var pickedUp : Boolean = false,
    val health: Double = 0.0,
    val attack: Double= 0.0,
    val defense : Double= 0.0,
    val healing : Double= 0.0,
    override var command: String = name.lowercase()
) : GameObject()