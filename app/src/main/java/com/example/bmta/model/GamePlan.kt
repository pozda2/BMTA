package com.example.bmta.model

import kotlin.random.Random

data class GamePlan (val width: Int, val height: Int, val numForrest: Int) {
    private var gamePlan = Array(height) { Array(width) { GameField(Terrain.MEADOW) } }

    constructor() : this(20, 10, 4)

    init {
        generateGamePlan()
    }

    private fun generateGamePlan(){
        // Borders
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (i == 0 || j == 0 || i == height -1 || j==width-1) {
                    gamePlan[i][j]= GameField(Terrain.BORDER)
                }
            }
        }

        // River
        val bridgePosition = generateRandomPosition()
        for (i in 1..height - 2 ) {
            gamePlan[i][bridgePosition.x]= GameField(Terrain.RIVER)
        }
        gamePlan[bridgePosition.y][bridgePosition.x]= GameField(Terrain.BRIDGE)

        // forests
        for (f in 1 .. numForrest) {
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

    fun generateFreeRandomPositionOnMeadow(gameObjects: ArrayList<Any>) : Position {
        var randomPosition : Position
        do {
            randomPosition = generateRandomPositionOnMeadow()
        } while (! randomPosition.isFree(gameObjects) )
        return randomPosition
    }

    fun getTerrainOnGameField (position: Position) : Terrain {
        return gamePlan[position.y][position.x].terrain
    }

    fun map() {
        for (i in 0 until height) {
            for (j in 0 until width) {
                print (gamePlan[i][j].terrain.terrainChar + " ")
            }
            println()
        }
    }

    fun map(hero: Hero) {
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (hero.position.y == i && hero.position.x == j) {
                    print ("H ")
                } else {
                    print(gamePlan[i][j].terrain.terrainChar + " ")
                }
            }
            println()
        }
    }

    fun map(gameObjects: ArrayList<Any>) {
        var printedObject : Boolean
        for (i in 0 until height) {
            for (j in 0 until width) {
                printedObject=false
                for (gameObject in gameObjects) {
                    if (gameObject is GameObject) {
                        if (gameObject.position == Position(j, i)) {
                            if (gameObject is Hero) {
                                print ("H ")
                                printedObject = true
                                break
                            }  else if (gameObject is Enemy && ! gameObject.isDeath()) {
                                print ("N ")
                                printedObject = true
                                break
                            }  else if (gameObject is Item && ! gameObject.pickedUp) {
                                print ("P ")
                                printedObject = true
                                break
                            }
                        }
                    }
                }

                if (! printedObject) print(gamePlan[i][j].terrain.terrainChar + " ")
            }
            println()
        }
    }
}