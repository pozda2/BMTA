package com.example.bmta.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bmta.R
import com.example.bmta.model.Item

class ItemAdaper (val items: ArrayList<Any> ) : RecyclerView.Adapter<ItemHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setImageData(items[position] as Item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}