package com.example.xenon.Adapter.Team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.DataClass.EveDataClass
import com.example.xenon.R

class EventsAdapter(private val context: Context, private val eve: MutableList<EveDataClass>) :
    RecyclerView.Adapter<EventsAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_wing, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val eveSection = eve[position]
        holder.eveName.text = eveSection.eveType
        holder.eveRv.adapter = Adapter(eveSection.eve)
        holder.eveRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getItemCount(): Int = eve.size

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eveName: TextView = itemView.findViewById(R.id.wingName)
        val eveRv: RecyclerView = itemView.findViewById(R.id.membersRv)
    }
}
