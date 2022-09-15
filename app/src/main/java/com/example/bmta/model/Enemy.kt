package com.example.bmta.model

data class Enemy(
    override var name: String ,
    override var position: Position,
    override var health: Float ,
    override var attack: Float ,
    override var defense: Float ,
    override var command: String,
    override var imgResource: Int
) : Character ()