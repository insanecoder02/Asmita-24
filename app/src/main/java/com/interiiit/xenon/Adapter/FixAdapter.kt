package com.interiiit.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.DataClass.FixtureDataClass.FixtureSportDataClass
import com.interiiit.xenon.R

class FixAdapter(
    private val day: MutableList<FixtureSportDataClass>,
    private val itemClickListener: com.interiiit.xenon.Fragment.Home
) : RecyclerView.Adapter<FixAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return day.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fix = day[position]
        holder.name.text = fix.type
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(day[position])
        }
    }
}