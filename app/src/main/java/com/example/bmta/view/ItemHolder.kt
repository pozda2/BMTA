package com.example.bmta.view

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmta.R
import com.example.bmta.model.Item

class ItemHolder (view: View) : RecyclerView.ViewHolder(view) {
    private var imageItem : ImageView?

    init {
        imageItem  =  view.findViewById(R.id.imageItem)
    }

    fun setImageData (item : Item) {
        imageItem?.setImageResource(item.imgResource)
    }
}