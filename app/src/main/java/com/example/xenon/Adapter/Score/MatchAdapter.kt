package com.example.xenon.Adapter.Score

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.DataClass.Score.MatchDetails
import com.example.xenon.R

class UpcomingMatchAdapter(
    private val sch: List<MatchDetails>
) : RecyclerView.Adapter<UpcomingMatchAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_schedule, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int = sch.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val scc = sch[position]

        holder.matName.text = scc.matchName
        holder.time.text = scc.time
        holder.name1.text = scc.clgName1
        holder.name2.text = scc.clgName2
        holder.date.text = scc.date

        Glide.with(holder.itemView.context)
            .load(scc.clgImg1)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.group) // Replace with your error drawable
            .into(holder.img1)

        Glide.with(holder.itemView.context)
            .load(scc.clgImg2)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.group)
            .into(holder.img2)
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matName: TextView = itemView.findViewById(R.id.matchName)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val name2: TextView = itemView.findViewById(R.id.clgName2)
        val name1: TextView = itemView.findViewById(R.id.clgName1)
        val img1: ImageView = itemView.findViewById(R.id.clgImg1)
        val img2: ImageView = itemView.findViewById(R.id.clgImg2)
    }
}



