package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xenon.DataClass.Gallery
import com.example.xenon.R

class GalleryAdapter(val context: Context, private val gallery: List<Gallery>):RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gall = gallery[position]

        holder.evenam.text = gall.name
        Glide.with(context)
            .load(gall.img)
            .into(holder.eveimg)
    }

    override fun getItemCount(): Int {
        return gallery.size
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val eveimg:ImageButton = itemView.findViewById(R.id.eve_img)
        val evenam:TextView = itemView.findViewById(R.id.eve_name)
    }
}