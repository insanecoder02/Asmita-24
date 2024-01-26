package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.DataClass.FlickrPhoto
import com.example.xenon.R

class GalleryAdapter(val context: Context, private val gallery: List<FlickrPhoto>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flickrPhoto = gallery[position]

        // Assuming 'img' is the property in FlickrPhoto containing the image URL
        val imageUrl =
            "https://farm${flickrPhoto.farm}.staticflickr.com/${flickrPhoto.server}/${flickrPhoto.id}_${flickrPhoto.secret}_m.jpg"

        // Load the image into the ImageButton using Glide
        Glide.with(context)
            .load(imageUrl)
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.eveimg)


    }

    override fun getItemCount(): Int {
        return gallery.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eveimg: ImageView = itemView.findViewById(R.id.eve_img)
        val evenam: TextView = itemView.findViewById(R.id.eve_name)

    }
}