package com.example.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.DataClass.FixtureDataClass
import com.example.xenon.R

class FixAdapter(
    private val day: MutableList<FixtureDataClass>,
    private val itemClickListener: com.example.xenon.Fragment.SeeAll
) : RecyclerView.Adapter<FixAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.dayText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_fix, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return day.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fix = day[position]
        holder.name.text = fix.day
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(day[position])
        }
    }
}