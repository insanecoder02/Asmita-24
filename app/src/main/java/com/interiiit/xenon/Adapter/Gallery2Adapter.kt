package com.interiiit.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.interiiit.xenon.DataClass.GalleryDataClass.Gallery2
import com.interiiit.xenon.R

class Gallery2Adapter(
    val sports: List<Gallery2>,
    private val itemClickListener: com.interiiit.xenon.Fragment.Gallery2
) : RecyclerView.Adapter<Gallery2Adapter.GalleryViewHolder>() {
    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sport_name = itemView.findViewById<TextView>(R.id.sport)
        var sport_img = itemView.findViewById<ImageView>(R.id.Sport_img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.layout_gallery2, parent, false)
        return GalleryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sports.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val sprt = sports[position]
        holder.sport_name.text = sprt.sport_name
        Glide.with(holder.itemView.context)
            .load(sports[position].sport_img)
            .thumbnail(0.5f)
            .transform(RoundedCorners(20))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.sport_img)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(sports[position % sports.size])
        }
    }
}


