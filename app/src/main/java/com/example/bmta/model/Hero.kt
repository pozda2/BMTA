package com.example.bmta.model

data class Hero(
    override var name: String = "Hrdina",
    override var position: Position,
    override var command: String = "",
    ) : Character() {

    constructor(name: String, position: Position, command: String = "", health: Double, attack : Double, defense: Double, healing : Double) : this (name, position, command) {
        this.health = health
        this.attack = attack
        this.defense = defense
        this.healing = healing
    }

    override var attack : Double = 1.2
        get() {
            var a = field
            for (item in this.items) a += item.attack

            return a
        }
    override var defense : Double = 1.2
        get() {
            var a = field
            for (item in this.items) a += item.defense
            return a
        }

    var healing: Double = 0.5
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

    fun addItem (item: Item) : String {
        items.add(item)
        item.pickedUp=true
        return "Sebrán ${item.name}"
    }
}