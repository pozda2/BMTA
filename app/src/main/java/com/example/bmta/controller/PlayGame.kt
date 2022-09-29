package com.example.bmta.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmta.databinding.ActivityPlayGameBinding
import com.example.bmta.model.*
import com.example.bmta.view.ItemAdaper
import com.example.bmta.view.LogAdapter
import java.util.*

class PlayGame : AppCompatActivity() {
    private lateinit var binding : ActivityPlayGameBinding
    private val logs = LinkedList(listOf(""))
    private lateinit var game : Game
    private var heroName : String = "hrdina"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras != null) {
            heroName = intent.extras!!.getString("heroName").toString()
        }

        // Když si budu delat instanci třídy po ručně, tak se při každém překlopení hra vytvoří znovu
        //game = Game(heroName)

        // Instanci hry si nechám vytvořit přes ViewModelProvider
        // Pro předání jména potřebuji factory
        val factory = GameFactory(heroName)
        game = ViewModelProvider(this, factory).get(Game::class.java)

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
        binding.itemsRecycler.adapter = ItemAdaper (game.hero.items)

        refreshStats()
        refreshGameFields()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun runRound (direction: Direction) {
        var message : String
        val command : String = game.getCommand(direction)

        if (command =="nop") {
            Toast.makeText(this, "Neplatný příkaz", Toast.LENGTH_SHORT).show()
            return
        } else {
            message = game.runCommand(command)
            if (message.isNotEmpty()) {
                //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                logs.push (message)
                binding.logRecycler.adapter?.notifyDataSetChanged()
            }
            binding.itemsRecycler.adapter?.notifyDataSetChanged()
        }

        message=game.enemyAttack()
        if (message.isNotEmpty()) {
            // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            logs.push (message)
        }

        game.heroHealing()

        message = game.gameFinish()
        if (message.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Konec hry")
                    .setMessage(message)
                    .setPositiveButton("OK") { _, _ ->
                        startActivity(Intent(this, MainActivity::class.java))
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        refreshStats()
        refreshGameFields()
    }

    private fun refreshGameFields() {
        binding.imageNorth.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.NORTH)).imgResource)

        binding.imageNorthEast.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.NORTHEAST)).imgResource)

        binding.imageEast.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.EAST)).imgResource)

        binding.imageSouthEast.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.SOUTHEAST)).imgResource)

        binding.imageSouth.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.SOUTH)).imgResource)

        binding.imageSouthWest.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.SOUTHWEST)).imgResource)

        binding.imageWest.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.WEST)).imgResource)

        binding.imageNorthWest.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.NORTHWEST)).imgResource)

        binding.imageCenter.setImageResource(
            Icons.valueOf(game.getObjectOnPosition(Direction.NOMOVE)).imgResource)
    }

    private fun refreshStats () {
        binding.textHeroName.text = game.heroName
        binding.textScore.text = game.score.toString()
        binding.textHealth.text= "%.2f".format(game.hero.health)
        binding.textAttack.text= "%.2f".format(game.hero.attack)
        binding.textDefense.text= "%.2f".format(game.hero.defense)
        binding.textHealing.text= "%.2f".format(game.hero.healing)
        binding.textKills.text = game.hero.kills.toString()
    }
}

