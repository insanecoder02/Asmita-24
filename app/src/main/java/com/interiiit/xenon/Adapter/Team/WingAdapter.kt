package com.interiiit.xenon.Adapter.Team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.DataClass.Team.TeamSection
import com.interiiit.xenon.R

class WingAdapter(val context: Context, private val teamSections: List<TeamSection>) :
    RecyclerView.Adapter<WingAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_wing, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val teamSection = teamSections[position]

        holder.wingName.text = teamSection.wingName
        holder.membersRv.adapter = MemberAdapter(teamSection.members)
        val spanCount = teamSection.members.size
        val layoutManager = GridLayoutManager(context, spanCount)
        holder.membersRv.layoutManager = layoutManager
    }

    override fun getItemCount(): Int = teamSections.size

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wingName: TextView = itemView.findViewById(R.id.wingName)
        val membersRv: RecyclerView = itemView.findViewById(R.id.membersRv)
    }
}