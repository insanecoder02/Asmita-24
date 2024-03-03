package com.interiiit.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.R

class Fixture_Day_Adapter(
    private val day: MutableList<Fixture_Day_DataClass>,
    private val itemClickListener: com.interiiit.xenon.Fragment.Fixture_Day_Wise
) : RecyclerView.Adapter<Fixture_Day_Adapter.ViewHolder>() {
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
//            itemClickListener.onItemClick(day[position])
        }
    }
}