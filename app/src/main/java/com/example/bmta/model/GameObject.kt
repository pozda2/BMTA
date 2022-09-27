package com.example.bmta.model

abstract class GameObject {
    abstract var name: String
    abstract var position: Position
    abstract var command: String
    abstract var imgResource : Int
}