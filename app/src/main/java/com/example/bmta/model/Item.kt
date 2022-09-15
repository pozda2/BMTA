package com.example.bmta.model

data class Item (
    override var name: String = "Item",
    override var position: Position,
    var pickedUp : Boolean = false,
    val oneUse : Boolean = false,
    var used : Boolean = false,
    val health: Float = 0.0F,
    val attack: Float= 0.0F,
    val defense : Float= 0.0F,
    val healing : Float= 0.0F,
    override var command: String,
    override var imgResource: Int) : GameObject() {

        fun useItem(hero: Hero) {
            if (oneUse and ! used) {
                hero.health = hero.health + health
                used = true
                pickedUp=true
            } else {
                hero.addItem(this)
            }
        }
    }