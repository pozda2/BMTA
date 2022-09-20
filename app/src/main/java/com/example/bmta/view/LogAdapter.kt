package com.example.bmta.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bmta.R

class LogAdapter (val logList : List<String>) : RecyclerView.Adapter<LogHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        return LogHolder(LayoutInflater.from(parent.context).inflate(R.layout.log_row, parent, false))
    }

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        holder.setLogData(logList[position])
    }

    override fun getItemCount(): Int {
        return logList.size
    }

}