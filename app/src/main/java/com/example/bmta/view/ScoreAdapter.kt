package com.example.bmta.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bmta.database.Score
import com.example.bmta.databinding.ScoreRowBinding

class ScoreAdapter() : ListAdapter<Score, ScoreAdapter.ScoreViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Score>() {
            override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
                return oldItem.dateAdded == newItem.dateAdded
            }

            override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val viewHolder = ScoreViewHolder(
            ScoreRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ScoreViewHolder(
        private var binding: ScoreRowBinding
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(score: Score) {
            binding.name.text = score.name
            binding.score.text = score.score.toString()
        }
    }
}