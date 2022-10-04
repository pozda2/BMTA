package com.example.bmta.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmta.database.ScoreDatabase
import com.example.bmta.databinding.ActivityScoreBinding
import com.example.bmta.view.ScoreAdapter
import kotlinx.coroutines.launch

class ScoreActivity : AppCompatActivity() {
    private lateinit var binding : ActivityScoreBinding
    private val scoreDatabase by lazy { ScoreDatabase.getDatabase(this).scoreDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scoreRecyclerView.layoutManager = LinearLayoutManager(this)
        val scoreAdapter = ScoreAdapter()
        binding.scoreRecyclerView.adapter = scoreAdapter
        lifecycle.coroutineScope.launch {
            scoreDatabase.getScores().collect() {
                scoreAdapter.submitList(it)
            }
        }

        binding.backBtn.setOnClickListener() {
            super.onBackPressed()
        }
    }
}