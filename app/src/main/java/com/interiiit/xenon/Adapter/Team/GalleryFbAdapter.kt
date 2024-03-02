package com.interiiit.xenon.Adapter.Team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.interiiit.xenon.DataClass.GalleryDataClass.GalleryFb
import com.interiiit.xenon.R

class GalleryFbAdapter(
    val context:Context,
    val list:MutableList<GalleryFb>
):RecyclerView.Adapter<GalleryFbAdapter.ViewHolder>() {
    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        val img: View? = item.findViewById(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryFbAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.gallerfb_item_view , parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryFbAdapter.ViewHolder, position: Int) {

        Glide
            .with(context)
            .load(list[position].img)
            .centerCrop()
            .into(holder.img as ImageView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

}