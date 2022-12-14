package com.example.bmta.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bmta.database.Score
import com.example.bmta.database.ScoreDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Game(
    val heroName: String ="Hrdina",
    settingsJson: String ="",
    val scoreDao: ScoreDao
) : ViewModel() {
    private var settings = Settings(settingsJson)
    // private var possibleCommands = arrayListOf<String>()
    // private var command: String = ""

    private var gamePlan = GamePlan(settings.width, settings.height, settings.numForests)
    lateinit var hero : Hero
    private var enemies = arrayListOf<GameObject>()
    private var items = arrayListOf<Item>()
    private var gameObjects = arrayListOf<GameObject>()
    var score = 0

    init {
        generateHero(settings.hero, heroName)
        generateEnemies(settings.enemies)
        generateItems(settings.items)
    }

    private fun generateHero(heroSettings: JSONObject?, heroName: String) {
        if (heroSettings != null) {

            hero = Hero (name = heroName,
                     position = gamePlan.generateRandomPositionOnMeadow(),
                     health = heroSettings.getDouble("health"),
                     attack = heroSettings.getDouble("attack"),
                     defense = heroSettings.getDouble("defense"),
                     healing = heroSettings.getDouble("healing")
            )
        } else {
            hero = Hero(name = heroName, position = gamePlan.generateRandomPositionOnMeadow())
        }

        gameObjects.add(hero)
    }

    private fun generateEnemies(enemies: JSONArray?) {
        var enemy : Enemy
        enemies?.let {
            for (i in 0 until enemies.length()) {
                val enemyType = enemies.getJSONObject(i)
                repeat (enemyType.getInt("count")) {
                    enemy = Enemy(name = enemyType.getString("name"),
                        position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
                        health = enemyType.getDouble("health"),
                        attack = enemyType.getDouble("attack"),
                        defense = enemyType.getDouble("defense"),
                    )
                    this.enemies.add(enemy)
                    gameObjects.add(enemy)
                }
            }
        }
    }

    private fun generateItems(items: JSONArray?) {
        var item : Item
        items?.let {
            for (i in 0 until items.length()) {
                val itemType = items.getJSONObject(i)
                item = Item(name = itemType.getString("name"),
                        position = gamePlan.generateFreeRandomPositionOnMeadow(gameObjects),
                        pickedUp = false,
                        health = itemType.getDouble("health"),
                        attack = itemType.getDouble("attack"),
                        defense = itemType.getDouble("defense"),
                        healing = itemType.getDouble("healing"),
                    )
                this.items.add(item)
                gameObjects.add(item)
            }
        }
    }

    private fun getEnemyOnGameField (position: Position, gameObjects: ArrayList<GameObject>) : Enemy? {
        for (obj in gameObjects ) {
            if (obj is Enemy) {
                if (obj.position == Position (position.x, position.y)) {
                    return obj
                }
            }
        }
        return null
    }

    private fun getItemOnGameField (position: Position, gameObjects: ArrayList<Item>) : Item? {
        for (obj in gameObjects ) {
            if (obj.position == Position (position.x, position.y)) {
                return obj
            }
        }
        return null
    }

    fun runCommand(command: String) : String {
        score++

        val enemy = getEnemyOnGameField(hero.position, enemies)
        val item = getItemOnGameField(hero.position, items)

        when (command) {
            "stav" -> return (hero.toString())
            "mapa" -> gamePlan.map(gameObjects)
            "utok" -> return if (enemy != null) {
                                hero.attack(enemy)
                            } else {
                                ""
                            }
        }

        enumValues<Direction>().forEach {
            if (it.command == command) return hero.go(it)
        }

        enumValues<Direction>().forEach {
            if ("kacej"+it.command == command) return hero.cutDown(it, gamePlan)
        }

        if (item != null && item.command.uppercase() == command.uppercase()) {
            return hero.addItem(item)
        }

        return ""
    }

    fun enemyAttack () : String {
        val result = StringBuilder ("")
        val enemy = getEnemyOnGameField(hero.position, enemies)
        if (enemy is Enemy) {
            if (!enemy.isDeath()) {
                result.append(enemy.attack(hero))
            }
        }
        return result.toString()
    }

    private fun allEnemiesDeath() : Boolean {
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

    fun gameFinish(): String {
        if (hero.isDeath()) return "Jsi mrtv??."
        if (allEnemiesDeath()) {
            Log.i("Konec", "konec")
            GlobalScope.launch {
                scoreDao.addScore(Score(0, Date(), hero.name, score))
            }
            return "V??ichni nep????tel?? jsou mrtv??. Vyhr??l jsi. Pot??eboval jsi $score tah??."
        }
        return ""
    }

    fun getCommand(direction: Direction) : String {
        var command : String

        if (direction == Direction.NOMOVE) {
            val enemy = getEnemyOnGameField(hero.position, enemies)
            val item = getItemOnGameField(hero.position, items)

            if (enemy is Enemy && !enemy.isDeath()) {
                return enemy.command
            } else if (item is Item && !item.pickedUp) {
                return item.command
            }
        } else {
            command = gamePlan.getGameField(
                Position(
                    hero.position.x + direction.relativeX,
                    hero.position.y + direction.relativeY
                )
            ).terrain.command
            if (command.isEmpty() || command == "kacej") {
                command += direction.command
                return command
            }
        }
        return "nop"
    }

    fun getObjectOnPosition (direction: Direction): String {
        val newPosition = Position(hero.position.x + direction.relativeX,
                                   hero.position.y + direction.relativeY)
        val enemy = getEnemyOnGameField(newPosition, enemies)
        val item = getItemOnGameField(newPosition, items)

        if (enemy is Enemy) {
            return if (enemy.isDeath()) {
                "Hrob"
            } else {
                enemy.name
            }
        } else if (item is Item && ! item.pickedUp) {
            return item.name
        }

        return gamePlan.getGameField(newPosition).terrain.toString()
    }

//    fun run() {
//        do {
//            possibleCommands.clear()
//            setPossibleCommands(hero)
//            println ("----------------------------------------------")
//            println (getSurroundingDescription())
//            println ("Mo??n?? p????kazy jsou: $possibleCommands")
//            command = readCommand()
//            println (runCommand(command))
//            println (enemyAttack())
//
//            score++
//
//            if (command.uppercase()=="KONEC") {
//                println ("Konec hry")
//                break
//            }
//
//            if (hero.isDeath()) {
//                println ("Jsi mrtv??. Hra kon????.")
//                break
//            }
//
//            if (allEnemiesDeath()) {
//                println ("V??ichni nep????tel?? jsou mrtv??. Vyhr??l jsi. Pot??eboval jsi $score tah??.")
//                break
//            }
//
//            heroHealing()
//        } while (true)
//    }
//    fun readHeroName() : String {
//        print ("Zadej jm??ho hrdiny: ")
//        return readLine().toString()
//    }
    //    fun setPossibleCommands(hero: Hero) {
//        if (enemy is Enemy && ! enemy!!.isDeath()) possibleCommands.add("utok")
//        if (item is Item && ! item!!.pickedUp) possibleCommands.add(item!!.name)
//        enumValues<Direction>().forEach {
//            if (gamePlan.getGameField(
//                    Position(
//                        hero.position.x + it.relativeX,
//                        hero.position.y + it.relativeY
//                    )
//                )
//                    .isWalkable()
//            ) possibleCommands.add(it.command)
//        }
//
//        enumValues<Direction>().forEach {
//            if (gamePlan.getGameField(
//                    Position(
//                        hero.position.x + it.relativeX,
//                        hero.position.y + it.relativeY
//                    )
//                ) .terrain == Terrain.FOREST
//            ) possibleCommands.add("kacej"+it.command)
//        }
//
//        possibleCommands.add("stav")
//        possibleCommands.add("mapa")
//        possibleCommands.add("konec")
//
//    }
//
//    fun getSurroundingDescription () : String {
//        val description = java.lang.StringBuilder("")
//        description.append ("Na severu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.NORTH.relativeX,
//                hero.position.y + Direction.NORTH.relativeY)).terrain.description+". ")
//        description.append ("Na severov??chodu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.NORTHEAST.relativeX,
//                hero.position.y + Direction.NORTHEAST.relativeY)).terrain.description+". ")
//        description.append ("Na v??chodu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.EAST.relativeX,
//                hero.position.y + Direction.EAST.relativeY)).terrain.description+". ")
//        description.append ("Na jihov??chodu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.SOUTHEAST.relativeX,
//                hero.position.y + Direction.SOUTHEAST.relativeY)).terrain.description+". ")
//        description.append ("Na jihu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.SOUTH.relativeX,
//                hero.position.y + Direction.SOUTH.relativeY)).terrain.description+". ")
//        description.append ("Na jihoz??padu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.SOUTHWEST.relativeX,
//                hero.position.y + Direction.SOUTHWEST.relativeY)).terrain.description+". ")
//        description.append ("Na z??padu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.WEST.relativeX,
//                hero.position.y + Direction.WEST.relativeY)).terrain.description+".")
//        description.append ("Na severoz??padu je "+ gamePlan.getGameField(
//            Position(hero.position.x + Direction.NORTHWEST.relativeX,
//                hero.position.y + Direction.NORTHWEST.relativeY)).terrain.description+".")
//
//        if (item != null && ! item!!.pickedUp) description.append("\nNa zemi vid???? ${item!!.name}.")
//        if (enemy != null && ! enemy!!.isDeath()) description.append("\nPozor ${enemy!!.name}.")
//        if (enemy != null && enemy!!.isDeath()) description.append("\nNa zemi vid???? mrtvolu ${enemy!!.name}.")
//
//        return description.toString()
//    }

//    fun checkCommand (command: String): Boolean {
//        for (possibleCommand in possibleCommands) {
//            if (command.uppercase() == possibleCommand.uppercase()) {
//                return true
//            }
//        }
//        println ("Nezn??m?? p????kaz. ")
//        println ("Mo??n?? p????kazy jsou: $possibleCommands")
//        return false
//    }

//    fun readCommand(): String {
//        var command:String
//        do {
//            print("Zadej p????kaz: ")
//            command = readLine().toString()
//        } while (! checkCommand(command))
//
//        return command
//    }
}