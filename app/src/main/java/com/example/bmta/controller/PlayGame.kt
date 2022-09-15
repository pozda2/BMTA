package com.example.bmta.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.bmta.R
import com.example.bmta.databinding.ActivityPlayGameBinding
import com.example.bmta.model.*
import com.example.bmta.model.Item

class PlayGame : AppCompatActivity() {
    private lateinit var binding : ActivityPlayGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageNorth.setOnClickListener {
            runRound(Direction.NORTH)
        }

        binding.imageSouth.setOnClickListener {
            runRound(Direction.SOUTH)
        }

        binding.imageWest.setOnClickListener {
            runRound(Direction.WEST)
        }

        binding.imageEast.setOnClickListener {
            runRound(Direction.EAST)
        }

        binding.imageCenter.setOnClickListener {
            runRound(Direction.NOMOVE)
        }

        refreshStats()
        refreshGameFields()
    }

    private fun runRound (direction: Direction) {
        var message : String
        var command = "nop"
        if (direction == Direction.NOMOVE) {
            val enemy = Newgame.game.getEnemyOnGameField(Newgame.game.hero.position, Newgame.game.enemies)
            val item = Newgame.game.getItemOnGameField (Newgame.game.hero.position, Newgame.game.items)
            if (enemy is Enemy && ! enemy.isDeath()) {
                command = enemy.command
                Newgame.game.enemy=enemy
            } else if (item is Item && ! item.pickedUp)  {
                Newgame.game.item = item
                command=item.command
            } else {
                command = "nop"
            }
        } else {
            command = Newgame.game.gamePlan.getTerrainOnGameField(
                Position(
                    Newgame.game.hero.position.x + direction.relativeX,
                    Newgame.game.hero.position.y + direction.relativeY
                )
            ).command
            if (command.isEmpty() || command == "kacej")
                command += direction.command
        }

        if (command =="nop") {
            Toast.makeText(this, "Neplatný příkaz", Toast.LENGTH_SHORT).show()
            return
        } else {
            message = Newgame.game.runCommand(command)
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        message=Newgame.game.enemyAttack()
        if (message.isNotEmpty()) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        Newgame.game.score++

        if (Newgame.game.hero.isDeath()) {
                println ("Jsi mrtvý. Hra končí.")
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Konec hry")
                    .setMessage("Jsi mrtvý.")
                    .setPositiveButton("OK") { _, _ ->
                        startActivity(Intent(this, MainActivity::class.java))
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        if (Newgame.game.allEnemiesDeath()) {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Konec hry")
                .setMessage("Všichni nepřátelé jsou mrtví. Vyhrál jsi. Potřeboval jsi ${Newgame.game.score} tahů.")
                .setPositiveButton("OK") { _, _ ->
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        Newgame.game.heroHealing()

        refreshStats()
        refreshGameFields()
    }

    private fun refreshGameFields() {
        binding.imageNorth.setImageResource(Newgame.game.gamePlan.getTerrainOnGameField(
            Position(Newgame.game.hero.position.x + Direction.NORTH.relativeX,
                     Newgame.game.hero.position.y + Direction.NORTH.relativeY)
        ).imgResource)
        binding.imageSouth.setImageResource(Newgame.game.gamePlan.getTerrainOnGameField(
            Position(Newgame.game.hero.position.x + Direction.SOUTH.relativeX,
                     Newgame.game.hero.position.y + Direction.SOUTH.relativeY)
        ).imgResource)
        binding.imageWest.setImageResource(Newgame.game.gamePlan.getTerrainOnGameField(
            Position(Newgame.game.hero.position.x + Direction.WEST.relativeX,
                     Newgame.game.hero.position.y + Direction.WEST.relativeY)
        ).imgResource)
        binding.imageEast.setImageResource(Newgame.game.gamePlan.getTerrainOnGameField(
            Position(Newgame.game.hero.position.x + Direction.EAST.relativeX,
                     Newgame.game.hero.position.y + Direction.EAST.relativeY)
        ).imgResource)

        val enemy = Newgame.game.getEnemyOnGameField(Newgame.game.hero.position, Newgame.game.enemies)
        val item = Newgame.game.getItemOnGameField(Newgame.game.hero.position, Newgame.game.items)

        if (enemy is Enemy) {
            if (enemy.isDeath()) binding.imageCenter.setImageResource(R.drawable.ic_rip)
            else if (enemy.name == "Skeleton") binding.imageCenter.setImageResource(R.drawable.ic_skeleton)
            else if (enemy.name == "Troll") binding.imageCenter.setImageResource(R.drawable.ic_troll)
        } else if (item is Item && ! item.pickedUp) {
            binding.imageCenter.setImageResource(item.imgResource)
        } else {
            binding.imageCenter.setImageResource(
                Newgame.game.gamePlan.getTerrainOnGameField(Newgame.game.hero.position).imgResource)
        }
    }

    private fun refreshStats () {
        binding.textHeroName.text = Newgame.game.heroName
        binding.textScore.text = Newgame.game.score.toString()
        binding.textHealth.text= "%.2f".format(Newgame.game.hero.health)
        binding.textAttack.text= "%.2f".format(Newgame.game.hero.attack)
        binding.textDefense.text= "%.2f".format(Newgame.game.hero.defense)
        binding.textHealing.text= "%.2f".format(Newgame.game.hero.healing)
        binding.textKills.text = Newgame.game.hero.kills.toString()
    }
}
