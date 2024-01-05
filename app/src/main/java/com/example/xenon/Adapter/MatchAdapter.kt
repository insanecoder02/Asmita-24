package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.DataClass.Matches
import com.example.xenon.R

class MatchAdapter(
    private val context: Context,
    private val match: List<Matches>
) : RecyclerView.Adapter<MatchAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.match_layout, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return match.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val mat = match[position]
        holder.type.text = mat.status

        holder.match.adapter = ScheduleAdapter(context, mat.match)
        holder.match.layoutManager = LinearLayoutManager(context)

    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val type: TextView = itemView.findViewById(R.id.matchText)
        val match: RecyclerView = itemView.findViewById(R.id.scheduleRV)
    }
}