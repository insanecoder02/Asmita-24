package com.example.xenon.Adapter.Team

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.DataClass.Events
import com.example.xenon.R

class Adapter(private val evee:List<Events>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = evee[position]
        holder.name.text = event.name

        Glide.with(holder.itemView.context)
            .load(event.image)
            .thumbnail(0.1f)
            .error(R.drawable.group)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.img)
    }

    override fun getItemCount(): Int = evee.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val img:ImageView = itemView.findViewById(R.id.mem_img)
    }
}