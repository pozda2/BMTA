package com.example.bmta.model

abstract class Character : GameObject() {
    open var health: Double = 100.0
    open var attack: Double = 1.2
    open var defense: Double = 1.0

    fun isDeath (): Boolean {
        return (health < 0.00001)
    }

    open fun attack (enemy: Character) : String {
        var realAttack = attack - enemy.defense
        if (realAttack < 0) realAttack=0.0
        enemy.health = enemy.health - realAttack

        if (enemy.isDeath()) return "${enemy.name} je mrtvý."
        return "$name zaútočil silou " + "%.2f".format(realAttack) +"."
    }

}