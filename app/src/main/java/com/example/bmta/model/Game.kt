package com.example.bmta.model

import kotlin.random.Random
import com.example.bmta.R

class Game (val heroName:String) {
    private var width = 20
    private var height = 10
    private var numForests = 4
    private var numEnemies = 5
    private var possibleCommands = arrayListOf<String>()
    private var command: String = ""
    var score = 0

    var enemy : Enemy? = null
    var item : Item? = null

    var gamePlan = GamePlan(width, height, numForests)
    var enemies = arrayListOf<GameObject>()
    var items = arrayListOf<Item>()
    var gameObjects = arrayListOf<GameObject>()
    var hero = Hero(name = heroName, position = gamePlan.generateRandomPositionOnMeadow(), "", R.drawable.ic_knight)

    init {
        gameObjects.add(hero)
        generateEnemies()
        generateItems()
    }

    private fun generateEnemies() {
        var enemy : Enemy
        repeat (numEnemies) {
            enemy = generateEnemy()
            enemies.add(enemy)
            gameObjects.add(enemy)
        }
    }

    private fun generateEnemy(): Enemy {
        return if (Random.nextInt(1, 10) < 8) Enemy(name="Skeleton",
            position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
            health = 10.0F,
            attack = 5.0F,
            defense = 0.5F,
            command = "utok",
            imgResource = R.drawable.ic_troll
        ) else Enemy(name="Troll",
            position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
            health = 10.0F,
            attack = 5.0F,
            defense = 0.5F,
            command = "utok",
            imgResource = R.drawable.ic_skeleton
        )
    }

    private fun generateItems() {
       var item = Item(
           name ="mec",
           position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
           pickedUp = false,
           health = .0f,
           attack = 4.0f,
           defense = 1.0f,
           healing = 0.0f,
           command="mec",
           imgResource = R.drawable.ic_sword
       )
        items.add(item)
        gameObjects.add(item)
        item = Item(
            name = "dyka",
            position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
            pickedUp = false,
            health = 0.0f,
            attack = 2.0f,
            defense = 1.0f,
            healing = 0.0f,
            command = "dyka",
            imgResource = R.drawable.ic_knife
        )
        items.add(item)
        gameObjects.add(item)
        item = Item(
            name = "stit",
            position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
            pickedUp = false,
            health = 0.0f,
            attack = 0.0f,
            defense = 2.0f,
            healing = 0.0f,
            command = "stit",
            imgResource = R.drawable.ic_shield
        )
        items.add(item)
        gameObjects.add(item)
        item = Item(
            name = "helma",
            position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
            pickedUp = false,
            health = 0.0f,
            attack = 0.0f,
            defense = 1.0f,
            healing = 0.0f,
            command = "helma",
            imgResource = R.drawable.ic_helmet
        )
        items.add(item)
        gameObjects.add(item)
        item=Item(
            name= "brneni",
            position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
            pickedUp = false,
            health = 0.0f,
            attack = 0.0f,
            defense = 3.0f,
            healing = 0.0f,
            command = "brneni",
            imgResource = R.drawable.ic_armor
        )
        items.add(item)
        gameObjects.add(item)
        item = Item(
            name = "lekarna",
            position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
            pickedUp = false,
            health = 0.0f,
            attack = 0.0f,
            defense = 2.0f,
            healing = 1.0f,
            command = "lekarna",
            imgResource = R.drawable.ic_medical
        )
        items.add(item)
        gameObjects.add(item)
    }

    fun getEnemyOnGameField (position: Position, gameObjects: ArrayList<GameObject>) : Enemy? {
        for (obj in gameObjects ) {
            if (obj is Enemy) {
                if (obj.position == Position (position.x, position.y)) {
                    return obj
                }
            }
        }
        return null
    }

    fun getItemOnGameField (position: Position, gameObjects: ArrayList<Item>) : Item? {
        for (obj in gameObjects ) {
            if (obj.position == Position (position.x, position.y)) {
                return obj
            }
        }
        return null
    }

    fun setPossibleCommands(hero: Hero) {
        if (enemy is Enemy && ! enemy!!.isDeath()) possibleCommands.add("utok")
        if (item is Item && ! item!!.pickedUp) possibleCommands.add(item!!.name)
        enumValues<Direction>().forEach {
            if (gamePlan.getGameField(
                    Position(
                        hero.position.x + it.relativeX,
                        hero.position.y + it.relativeY
                    )
                )
                    .isWalkable()
            ) possibleCommands.add(it.command)
        }

        enumValues<Direction>().forEach {
            if (gamePlan.getGameField(
                    Position(
                        hero.position.x + it.relativeX,
                        hero.position.y + it.relativeY
                    )
                ) .terrain == Terrain.FOREST
            ) possibleCommands.add("kacej"+it.command)
        }

        possibleCommands.add("stav")
        possibleCommands.add("mapa")
        possibleCommands.add("konec")

    }

    fun getSurroundingDescription () : String {
        val description = java.lang.StringBuilder("")
        description.append ("Na severu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.NORTH.relativeX,
                hero.position.y + Direction.NORTH.relativeY)).terrain.description+". ")
        description.append ("Na severovýchodu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.NORTHEAST.relativeX,
                hero.position.y + Direction.NORTHEAST.relativeY)).terrain.description+". ")
        description.append ("Na východu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.EAST.relativeX,
                hero.position.y + Direction.EAST.relativeY)).terrain.description+". ")
        description.append ("Na jihovýchodu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.SOUTHEAST.relativeX,
                hero.position.y + Direction.SOUTHEAST.relativeY)).terrain.description+". ")
        description.append ("Na jihu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.SOUTH.relativeX,
                hero.position.y + Direction.SOUTH.relativeY)).terrain.description+". ")
        description.append ("Na jihozápadu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.SOUTHWEST.relativeX,
                hero.position.y + Direction.SOUTHWEST.relativeY)).terrain.description+". ")
        description.append ("Na západu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.WEST.relativeX,
                hero.position.y + Direction.WEST.relativeY)).terrain.description+".")
        description.append ("Na severozápadu je "+ gamePlan.getGameField(
            Position(hero.position.x + Direction.NORTHWEST.relativeX,
                hero.position.y + Direction.NORTHWEST.relativeY)).terrain.description+".")

        if (item != null && ! item!!.pickedUp) description.append("\nNa zemi vidíš ${item!!.name}.")
        if (enemy != null && ! enemy!!.isDeath()) description.append("\nPozor ${enemy!!.name}.")
        if (enemy != null && enemy!!.isDeath()) description.append("\nNa zemi vidíš mrtvolu ${enemy!!.name}.")

        return description.toString()
    }

    fun checkCommand (command: String): Boolean {
        for (possibleCommand in possibleCommands) {
            if (command.uppercase() == possibleCommand.uppercase()) {
                return true
            }
        }
        println ("Neznámý příkaz. ")
        println ("Možné příkazy jsou: $possibleCommands")
        return false
    }

    fun readCommand(): String {
        var command:String
        do {
            print("Zadej příkaz: ")
            command = readLine().toString()
        } while (! checkCommand(command))

        return command
    }

    fun runCommand(command: String) : String {
        when (command) {
            "stav" -> return (hero.toString())
            "mapa" -> gamePlan.map(gameObjects)
            "utok" -> return (hero.attack(enemy!!))
        }

        enumValues<Direction>().forEach {
            if (it.command == command) return hero.go(it)
        }

        enumValues<Direction>().forEach {
            if ("kacej"+it.command == command) return hero.cutDown(it, gamePlan)
        }

        if (item is Item && item!!.name == command) {
            hero.addItem(item!!)
        }

        return ""
    }

    fun readHeroName() : String {
        print ("Zadej jmého hrdiny: ")
        return readLine().toString()
    }

    fun enemyAttack () : String {
        val result = StringBuilder ("")
        if (enemy is Enemy) {
            if (!enemy!!.isDeath()) {
                result.append(enemy!!.attack(hero))
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

    fun run() {
        do {
            possibleCommands.clear()
            setPossibleCommands(hero)
            println ("----------------------------------------------")
            println (getSurroundingDescription())
            println ("Možné příkazy jsou: $possibleCommands")
            command = readCommand()
            println (runCommand(command))
            println (enemyAttack())

            score++

            if (command.uppercase()=="KONEC") {
                println ("Konec hry")
                break
            }

            if (hero.isDeath()) {
                println ("Jsi mrtvý. Hra končí.")
                break
            }

            if (allEnemiesDeath()) {
                println ("Všichni nepřátelé jsou mrtví. Vyhrál jsi. Potřeboval jsi $score tahů.")
                break
            }

            heroHealing()
        } while (true)
    }
}