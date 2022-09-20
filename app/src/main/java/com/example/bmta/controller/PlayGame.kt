package com.example.bmta.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmta.R
import com.example.bmta.databinding.ActivityPlayGameBinding
import com.example.bmta.model.*
import com.example.bmta.view.ItemAdaper
import com.example.bmta.view.LogAdapter
import java.util.*

class PlayGame : AppCompatActivity() {
    private lateinit var binding : ActivityPlayGameBinding

    private val logs = LinkedList(listOf(""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageNorth.setOnClickListener {
            runRound(Direction.NORTH)
        }

        binding.imageNorthEast.setOnClickListener {
            runRound(Direction.NORTHEAST)
        }

        binding.imageEast.setOnClickListener {
            runRound(Direction.EAST)
        }

        binding.imageSouthEast.setOnClickListener {
            runRound(Direction.SOUTHEAST)
        }

        binding.imageSouth.setOnClickListener {
            runRound(Direction.SOUTH)
        }

        binding.imageSouthWest.setOnClickListener {
            runRound(Direction.SOUTHWEST)
        }

        binding.imageWest.setOnClickListener {
            runRound(Direction.WEST)
        }

        binding.imageNorthWest.setOnClickListener {
            runRound(Direction.NORTHWEST)
        }

        binding.imageCenter.setOnClickListener {
            runRound(Direction.NOMOVE)
        }

        binding.logRecycler.layoutManager = LinearLayoutManager(this)
        binding.logRecycler.adapter = LogAdapter(logs)
        binding.itemsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.itemsRecycler.adapter = ItemAdaper (Newgame.game.hero.items)

        refreshStats()
        refreshGameFields()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun runRound (direction: Direction) {
        var message : String
        var command : String
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
                //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                logs.push (message)
                binding.logRecycler.adapter?.notifyDataSetChanged()
            }
            binding.itemsRecycler.adapter?.notifyDataSetChanged()
        }

        message=Newgame.game.enemyAttack()
        if (message.isNotEmpty()) {
            // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            logs.push (message)
        }

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
        binding.imageNorth.setImageResource(getImageResourceOnGameField (
            Position(Newgame.game.hero.position.x + Direction.NORTH.relativeX,
                     Newgame.game.hero.position.y + Direction.NORTH.relativeY)
        ))

        binding.imageNorthEast.setImageResource(getImageResourceOnGameField (
            Position(Newgame.game.hero.position.x + Direction.NORTHEAST.relativeX,
                Newgame.game.hero.position.y + Direction.NORTHEAST.relativeY)
        ))

        binding.imageEast.setImageResource(getImageResourceOnGameField(
            Position(Newgame.game.hero.position.x + Direction.EAST.relativeX,
                Newgame.game.hero.position.y + Direction.EAST.relativeY)
        ))

        binding.imageSouthEast.setImageResource(getImageResourceOnGameField(
            Position(Newgame.game.hero.position.x + Direction.SOUTHEAST.relativeX,
                Newgame.game.hero.position.y + Direction.SOUTHEAST.relativeY)
        ))

        binding.imageSouth.setImageResource(getImageResourceOnGameField(
            Position(Newgame.game.hero.position.x + Direction.SOUTH.relativeX,
                     Newgame.game.hero.position.y + Direction.SOUTH.relativeY)
        ))

        binding.imageSouthWest.setImageResource(getImageResourceOnGameField(
            Position(Newgame.game.hero.position.x + Direction.SOUTHWEST.relativeX,
                Newgame.game.hero.position.y + Direction.SOUTHWEST.relativeY)
        ))

        binding.imageWest.setImageResource(getImageResourceOnGameField(
            Position(Newgame.game.hero.position.x + Direction.WEST.relativeX,
                     Newgame.game.hero.position.y + Direction.WEST.relativeY)
        ))
        binding.imageNorthWest.setImageResource(getImageResourceOnGameField(
            Position(Newgame.game.hero.position.x + Direction.NORTHWEST.relativeX,
                Newgame.game.hero.position.y + Direction.NORTHWEST.relativeY)
        ))

        binding.imageCenter.setImageResource(getImageResourceOnGameField(
            Position(Newgame.game.hero.position.x,
                Newgame.game.hero.position.y)
        ))
    }

    private fun getImageResourceOnGameField (position: Position) : Int {
        val enemy = Newgame.game.getEnemyOnGameField(position, Newgame.game.enemies)
        val item = Newgame.game.getItemOnGameField(position, Newgame.game.items)

        if (enemy is Enemy) {
            if (enemy.isDeath()) return R.drawable.ic_rip
            else if (enemy.name == "Skeleton") return R.drawable.ic_skeleton
            else if (enemy.name == "Troll") return R.drawable.ic_troll
        } else if (item is Item && ! item.pickedUp) {
            return item.imgResource
        }

        return Newgame.game.gamePlan.getTerrainOnGameField(position).imgResource
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
