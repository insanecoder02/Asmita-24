package com.example.xenon.Adapter

import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xenon.DataClass.ParticipateIIITS


class LeaderAdapter(
    private val usr: MutableList<ParticipateIIITS>
) : RecyclerView.Adapter<LeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.xenon.R.layout.layout_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return usr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = usr[position]
        Glide.with(holder.itemView.context).load(users.logo).thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().into(holder.logo)

        val ranking = position + 3
        holder.score.text = users.Points.toString()
        holder.name.text = users.Name
        holder.rank.text = ranking.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(com.example.xenon.R.id.teamName)
        val logo: ImageView = itemView.findViewById(com.example.xenon.R.id.teamLogo)
        val score: TextView = itemView.findViewById(com.example.xenon.R.id.teamScore)
        val rank: TextView = itemView.findViewById(com.example.xenon.R.id.teamRank)
    }
}