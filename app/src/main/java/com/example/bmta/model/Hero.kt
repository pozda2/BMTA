package com.example.bmta.model

import java.util.*

data class Hero(
    override var name: String = "Hrdina",
    override var position: Position,
    override var command: String = "",
    override var imgResource: Int
    ) : Character() {


    override var attack : Float = 1.2f
        get() {
            var a = field
            for (item in this.items) {
                if (item is Item) a += item.attack
            }
            return a
        }
    override var defense : Float = 1.2f
        get() {
            var a = field
            for (item in this.items) {
                if (item is Item) a += item.defense
            }
            return a
        }
    var healing: Float = 0.5F
        get() {
            var a = field
            for (item in this.items) {
                if (item is Item) a += item.healing
            }
            return a
        }

    var kills: Int =0
    var items = arrayListOf<Any>()

    override fun toString(): String {
        val description = StringBuilder("")
        description.append("Stav hrdiny\n")
        description.append("===========\n")
        description.append("Jméno        $name \n")
        description.append("Zdraví:      "+ "%.2f".format(health) + "\n")
        description.append("Útok:        "+ "%.2f".format(attack) + "\n")
        description.append("Obrana:      "+ "%.2f".format(defense) + "\n")
        description.append("Uzdravování: "+ "%.2f".format(healing) + "\n")
        description.append("Zabití:      $kills \n")
        description.append("Předměty:    ")
        for (item in items) {
            if (item is Item && ! item.used) description.append("${item.name}, ")
        }
        description.append("\n")
        return description.toString()
    }

    fun goNorth() {
        position.y = position.y - 1
    }

    fun goSouth() {
        position.y = position.y + 1
    }

    fun goEast() {
        position.x = position.x + 1
    }

    fun goWest() {
        position.x = position.x - 1
    }

    override fun attack(enemy: Character): String {
        val result = super.attack(enemy)
        if (enemy.isDeath()) kills+=1
        return result
    }

    fun cutDown(direction: String, gamePlan: GamePlan) {
        var x: Int = position.x
        var y: Int = position.y
        when (direction.lowercase(Locale.getDefault())) {
            "sever" -> y -= 1
            "jih" -> y += 1
            "vychod" -> x += 1
            "zapad" -> x -= 1
        }
        gamePlan.getGameField(Position (x, y)).terrain = Terrain.MEADOW
    }

    fun addItem (item: Item) {
        items.add(item)
        item.pickedUp=true
    }

}