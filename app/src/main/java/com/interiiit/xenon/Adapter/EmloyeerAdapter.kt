package com.interiiit.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.interiiit.xenon.Fragment.IIITEData
import com.interiiit.xenon.R

class EmloyeerAdapter(
    private val empl: MutableList<IIITEData>
): RecyclerView.Adapter<EmloyeerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(com.interiiit.xenon.R.id.teamName)
        val logo: ImageView = itemView.findViewById(com.interiiit.xenon.R.id.teamLogo)
        val score: TextView = itemView.findViewById(com.interiiit.xenon.R.id.teamScore)
        val rank: TextView = itemView.findViewById(com.interiiit.xenon.R.id.teamRank)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmloyeerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.interiiit.xenon.R.layout.layout_leaderboard, parent, false)
        return EmloyeerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return empl.size
    }

    override fun onBindViewHolder(holder: EmloyeerAdapter.ViewHolder, position: Int) {
        val users = empl[position]
        Glide.with(holder.itemView.context)
            .load(users.Logo)
            .thumbnail(0.1f)
            .error(R.drawable.group)
            .placeholder(R.drawable.place_leader)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .into(holder.logo)

        holder.score.text = users.Points.toString()
        holder.name.text = users.Name
        val ranking = position + 4
        holder.score.text = users.Points.toString()
        holder.name.text = users.Name
        holder.rank.text = ranking.toString()
    }
}