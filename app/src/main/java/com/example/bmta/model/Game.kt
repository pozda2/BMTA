package com.example.bmta.model

import kotlin.random.Random
class Game (val heroName:String) {
    private var width = 20
    private var height = 10
    private var numForests = 4
    private var numEnemies = 5
    private var possibleCommands = arrayListOf<String>()
    private var command: String = ""
    var score = 0

    lateinit var enemy : Enemy

    var gamePlan = GamePlan(width, height, numForests)
    var enemies = arrayListOf<Any>()
    private var gameObjects = arrayListOf<Any>()
    var hero = Hero(name=heroName, position = gamePlan.generateRandomPositionOnMeadow())

    init {
        gameObjects.add(hero)
        generateEnemies()
    }

    private fun generateEnemies() {
        var enemy : Enemy
        var random : Int
        for (i in 1..numEnemies) {
            random = Random.nextInt(1, 10)
            enemy = if (random < 8) {
                Skeleton(position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects))
            } else {
                Troll(position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects))
            }
            enemies.add(enemy)
            gameObjects.add(enemy)
        }
    }

    fun getEnemyOnGameField (position: Position, gameObjects: ArrayList<Any>) : Enemy? {
        for (obj in gameObjects ) {
            if (obj is Enemy) {
                if (obj.position == Position (position.x, position.y)) {
                    return obj
                }
            }
        }
        return null
    }

    fun setPossibleCommands(hero: Hero, gameObjects: ArrayList<Any>) {
        // todo use getEnemyOnGameField

        for (obj in gameObjects ) {
            if (obj is Enemy) {
                if (obj.position == hero.position && ! obj.isDeath()) {
                    possibleCommands.add("zautoc")
                    enemy = obj
                }
            }
        }

        if (gamePlan.getGameField(Position (hero.position.x, hero.position.y-1)).isWalkable()) possibleCommands.add("sever")
        if (gamePlan.getGameField(Position (hero.position.x, hero.position.y + 1)).isWalkable()) possibleCommands.add("jih")
        if (gamePlan.getGameField(Position (hero.position.x + 1, hero.position.y )).isWalkable()) possibleCommands.add("vychod")
        if (gamePlan.getGameField(Position (hero.position.x -1, hero.position.y )).isWalkable())  possibleCommands.add("zapad")

        if (gamePlan.getGameField(Position (hero.position.x, hero.position.y-1)).terrain == Terrain.FOREST) possibleCommands.add("kacejsever")
        if (gamePlan.getGameField(Position (hero.position.x, hero.position.y + 1)).terrain == Terrain.FOREST) possibleCommands.add("kacejjih")
        if (gamePlan.getGameField(Position (hero.position.x + 1, hero.position.y )).terrain == Terrain.FOREST) possibleCommands.add("kacejvychod")
        if (gamePlan.getGameField(Position (hero.position.x -1, hero.position.y )).terrain == Terrain.FOREST) possibleCommands.add("kacejzapad")

        possibleCommands.add("stav")
        possibleCommands.add("mapa")
        possibleCommands.add("konec")
    }

    fun runCommand(command: String) : String{
        when (command) {
//            "stav" -> println (hero.toString())
//            "mapa" -> gamePlan.map(gameObjects)
            "sever" ->  hero.goNorth()
            "jih" -> hero.goSouth()
            "zapad" -> hero.goWest()
            "vychod" ->  hero.goEast()
            "zautoc" -> return (hero.attack(enemy))
            "kacejsever" -> hero.cutDown("sever", gamePlan)
            "kacejjih" -> hero.cutDown("jih", gamePlan)
            "kacejzapad" -> hero.cutDown("zapad", gamePlan)
            "kacejvychod" -> hero.cutDown("vychod", gamePlan)
        }
        return ""
    }

    fun enemyAttack () : String {
        val result = StringBuilder ("")
        val enemy : Enemy? = getEnemyOnGameField (hero.position, gameObjects)

        if (enemy is Enemy) {
            if (!enemy.isDeath()) {
                result.append(enemy.attack(hero))
            }
        }
        return result.toString()
    }

    fun allEnemiesDeath() : Boolean {
        for (enemy in enemies) {
            if (enemy is Enemy && ! enemy.isDeath()) {
                return false
            }
        }
        return true
    }

    fun heroHealing() {
        if (hero.health < 100) {
            hero.health=hero.health + hero.healing
        }
    }

//    fun run() {
//        do {
//            possibleCommands.clear()
//            setPossibleCommands(hero, gameObjects)
//            println ("----------------------------------------------")
//            println (gamePlan.getSurroundingDescription(hero.position))
//            println ("Možné příkazy jsou: $possibleCommands")
//            command = readCommand()
//            println(runCommand(command))
//            println (enemyAttackAfterHero())
//
//            score++
//
//            if (command.uppercase()=="KONEC") {
//                println ("Konec hry")
//                break
//            }
//
//            if (hero.isDeath()) {
//                println ("Jsi mrtvý. Hra končí.")
//                break
//            }
//
//            if (allEnemiesDeath()) {
//                println ("Všichni nepřátelé jsou mrtví. Vyhrál jsi. Potřeboval jsi $score tahů.")
//                break
//            }
//
//            heroHealing()
//        } while (true)
//    }
}