package com.example.bmta.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bmta.databinding.ActivityNewgameBinding

class Newgame : AppCompatActivity() {
    private lateinit var binding : ActivityNewgameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewgameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            if (binding.heroNameEdit.text.isEmpty()) {
                Toast.makeText(this, "Zadejte jméno hrdiny.",Toast.LENGTH_LONG).show()
            } else {
                val i = Intent(this, PlayGame::class.java)
                i.putExtra("heroName", binding.heroNameEdit.text.toString())
                startActivity(i)
            }
        }
    }
}