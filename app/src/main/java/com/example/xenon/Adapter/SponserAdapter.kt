package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.DataClass.Sponser
import com.example.xenon.Fragment.Sponsors
import com.example.xenon.R

class SponserAdapter(
    private val context: Context,
    private val spons: MutableList<Sponser>
) : RecyclerView.Adapter<SponserAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.sponsText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sponsors_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return spons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spo = spons[position]

        holder.name.text = spo.name
    }
}