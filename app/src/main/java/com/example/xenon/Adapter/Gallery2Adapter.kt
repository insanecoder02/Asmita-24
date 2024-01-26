package com.example.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.DataClass.Gallery2
import com.example.xenon.R


class Gallery2Adapter(val sports:List<Gallery2>,private val itemClickListener: com.example.xenon.Fragment.Gallery2):RecyclerView.Adapter<Gallery2Adapter.GalleryViewHolder>(){
    class GalleryViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var sport_name=itemView.findViewById<TextView>(R.id.sport)
        var sport_img=itemView.findViewById<ImageView>(R.id.Sport_img)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflator=LayoutInflater.from(parent.context)
        val view=inflator.inflate(R.layout.gallery2,parent,false)
        return GalleryViewHolder(view)
    }

    override fun getItemCount(): Int {
       return sports.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val sees = sports[position %sports.size]
//        holder.bind(sees)
        holder.sport_name.text=sports[position].sport_name
        Glide.with(holder.itemView.context)
            .load(sports[position].sport_img)
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.sport_img)
        holder.itemView.setOnClickListener{
//            itemClickListener.onItemClick(sees)
        }
    }
}


