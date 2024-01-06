package com.example.xenon.Adapter.Team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.DataClass.Team.TeamSection
import com.example.xenon.R

class WingAdapter(val context: Context, private val teamSections: List<TeamSection>) :
    RecyclerView.Adapter<WingAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wing_layout, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val teamSection = teamSections[position]

        holder.wingName.text = teamSection.wingName

        holder.membersRv.adapter = MemberAdapter(teamSection.members)
        holder.membersRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getItemCount(): Int = teamSections.size

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wingName: TextView = itemView.findViewById(R.id.wingName)
        val membersRv: RecyclerView = itemView.findViewById(R.id.membersRv)
    }
}