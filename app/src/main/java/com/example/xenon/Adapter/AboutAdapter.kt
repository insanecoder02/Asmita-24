package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.xenon.DataClass.AboutUs
import com.example.xenon.R

class AboutAdapter(
    private val abt: List<AboutUs>
) : RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_about, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return abt.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val about = abt[position]

        holder.info.text = about.info
        Glide.with(holder.itemView.context)
            .load(about.img)
            .thumbnail(0.1f)
            .transform(CenterCrop(), RoundedCorners(20))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.group)
            .into(holder.img)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val info: TextView = itemView.findViewById(R.id.info)
        val img: ImageView = itemView.findViewById(R.id.img)
    }
}