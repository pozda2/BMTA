package com.example.bmta.model

import kotlin.random.Random

data class GamePlan (val width: Int = 20, val height : Int = 10, val numForrest: Int = 4) {
    private var gamePlan = Array(height) { Array(width) { GameField(Terrain.MEADOW) } }

    init {
        generateGamePlan()
    }

    private fun generateGamePlan() {
        generateBorder()
        generateRiver()
        repeat(numForrest) {
            generateForrest()
        }
    }

    private fun generateBorder() {
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (i == 0 || j == 0 || i == height - 1 || j == width - 1) {
                    gamePlan[i][j] = GameField(Terrain.BORDER)
                }
            }
        }
    }

    private fun generateRiver() {
        val bridgePosition = generateRandomPosition()
        for (i in 1..height - 2) {
            gamePlan[i][bridgePosition.x] = GameField(Terrain.RIVER)
        }
        gamePlan[bridgePosition.y][bridgePosition.x] = GameField(Terrain.BRIDGE)
    }

    private fun generateForrest() {
        val forrestPosition = generateRandomPositionOnMeadow()
        gamePlan[forrestPosition.y][forrestPosition.x].terrain = Terrain.FOREST
        if (gamePlan[forrestPosition.y - 1][forrestPosition.x].terrain == Terrain.MEADOW) {
            gamePlan[forrestPosition.y - 1][forrestPosition.x].terrain = Terrain.FOREST
        }
        if (gamePlan[forrestPosition.y + 1][forrestPosition.x].terrain == Terrain.MEADOW) {
            gamePlan[forrestPosition.y + 1][forrestPosition.x].terrain = Terrain.FOREST
        }
        if (gamePlan[forrestPosition.y][forrestPosition.x - 1].terrain == Terrain.MEADOW) {
            gamePlan[forrestPosition.y][forrestPosition.x - 1].terrain = Terrain.FOREST
        }
        if (gamePlan[forrestPosition.y][forrestPosition.x + 1].terrain == Terrain.MEADOW) {
            gamePlan[forrestPosition.y][forrestPosition.x + 1].terrain = Terrain.FOREST
        }
    }

    private fun generateRandomCoord (size: Int) : Int {
        return Random.nextInt(1, size -1)
    }

    fun getGameField (position : Position) : GameField {
        return gamePlan[position.y][position.x]
    }

    private fun generateRandomPosition () : Position {
        return Position(generateRandomCoord(width), generateRandomCoord(height))
    }

    fun generateRandomPositionOnMeadow() : Position {
        var randomPosition : Position
        do {
            randomPosition = generateRandomPosition()
        } while (getGameField(randomPosition).terrain != Terrain.MEADOW)
        return randomPosition
    }

    fun generateFreeRandomPositionOnMeadow(gameObjects: ArrayList<GameObject>) : Position {
        var randomPosition : Position
        do {
            randomPosition = generateRandomPositionOnMeadow()
        } while (! randomPosition.isFree(gameObjects) )
        return randomPosition
    }

    fun map(gameObjects: ArrayList<GameObject>) {
        var printedObject: Boolean
        for (i in 0 until height) {
            for (j in 0 until width) {
                printedObject = false
                for (gameObject in gameObjects) {
                    if (gameObject.position == Position(j, i)) {
                        if (gameObject is Hero) {
                            print("H ")
                            printedObject = true
                            break
                        } else if (gameObject is Enemy && !gameObject.isDeath()) {
                            print("N ")
                            printedObject = true
                            break
                        } else if (gameObject is Item && !gameObject.pickedUp) {
                            print("P ")
                            printedObject = true
                            break
                        }
                    }
                }
                if (!printedObject) print(gamePlan[i][j].terrain.terrainChar + " ")
            }
            println()
        }
    }
}