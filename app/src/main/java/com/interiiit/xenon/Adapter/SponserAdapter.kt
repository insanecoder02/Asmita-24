package com.interiiit.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.interiiit.xenon.DataClass.Sponser
import com.interiiit.xenon.R

class SponserAdapter(
    private val spons: MutableList<Sponser>
) : RecyclerView.Adapter<SponserAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img:ImageView = itemView.findViewById(R.id.sponImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_sponsors, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return spons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spo = spons[position]
        Glide.with(holder.itemView.context)
            .load(spo.img)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.group)
            .placeholder(R.drawable.group)
            .into(holder.img)
    }
}