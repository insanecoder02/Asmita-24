package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xenon.DataClass.MatchDetails
import com.example.xenon.R

class ScheduleAdapter(private val context: Context, private val sch: List<MatchDetails>) :
    RecyclerView.Adapter<ScheduleAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.schedule_layout, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return sch.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val scc = sch[position]

        holder.matName.text = scc.matchName
        holder.date.text = scc.matchName
        holder.time.text = scc.matchName
        holder.name1.text = scc.matchName
        holder.name2.text = scc.matchName
        Glide.with(context)
            .load(scc.clgImg1)
            .into(holder.img1)
        Glide.with(context)
            .load(scc.clgImg2)
            .into(holder.img2)
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matName: TextView = itemView.findViewById(R.id.matchName)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val name2: TextView = itemView.findViewById(R.id.clgName2)
        val name1: TextView = itemView.findViewById(R.id.clgName1)
        val img1: ImageView = itemView.findViewById(R.id.clgImg1)
        val img2: ImageView = itemView.findViewById(R.id.clgImg2)

    }

}