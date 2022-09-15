package com.example.bmta.model

abstract class GameObject {
    open lateinit var name: String
    open lateinit var position: Position
    open lateinit var command: String
    open var imgResource : Int = 0
}