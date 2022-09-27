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
            for (item in this.items) a += item.attack

            return a
        }
    override var defense : Float = 1.2f
        get() {
            var a = field
            for (item in this.items) a += item.defense
            return a
        }

    var healing: Float = 0.5F
        get() {
            var a = field
            for (item in this.items) a += item.healing
            return a
        }

    var kills: Int = 0
    var items = arrayListOf<Item>()

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
            description.append("${item.name}, ")
        }
        description.append("\n")

        return description.toString()
    }

    fun go (direction: Direction) : String {
        position.x += direction.relativeX
        position.y += direction.relativeY
        return "Jdu na ${direction.description}"
    }

    override fun attack(enemy: Character): String {
        val result = super.attack(enemy)
        if (enemy.isDeath()) kills += 1
        return result
    }

    fun cutDown(direction: Direction, gamePlan: GamePlan) : String {
        val x: Int = position.x + direction.relativeX
        val y: Int = position.y + direction.relativeY
        gamePlan.getGameField(Position (x, y)).terrain = Terrain.MEADOW
        return "Skácen les směrem " + direction.description
    }

    fun addItem (item: Item) {
        items.add(item)
        item.pickedUp=true
    }
}