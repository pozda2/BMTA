package com.example.bmta.model

abstract class Character {
    open lateinit var name: String
    open lateinit var position: Position
    open  var health: Float = 100f
        set(value) {
            field = if (value > 100) {
                100f
            } else {
                value
            }
        }
    open var attack: Float = 1.2f
    open var defense: Float = 1f

    fun isDeath (): Boolean {
        return (health < 0.00001)
    }
    open fun increaseAttack() {}
    open fun increaseDefense() {}

    open fun attack (enemy: Character) : String {
        var realAttack = attack - enemy.defense
        if (realAttack < 0) realAttack=0.0f

        enemy.health = enemy.health - realAttack
        if (enemy.isDeath()) return "${enemy.name} je mrtvý."

        enemy.increaseDefense()
        return "$name zaútočil silou " + "%.2f".format(realAttack) +"."
    }
}