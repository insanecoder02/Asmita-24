package com.example.xenon.Adapter.EventsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.DataClass.EventDataClass.Events
import com.example.xenon.R

class FeaturedEventsAdapter(
    val context: Context,
    val datalist: MutableList<Events>,
    private val itemClickListener: com.example.xenon.Fragment.Event
) : RecyclerView.Adapter<FeaturedEventsAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_event, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.count()
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = datalist[position]
        holder.name.text = item.name
        holder.date.text = item.date
        Glide.with(holder.itemView.context)
            .load(item.image)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.group)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(datalist[position])
        }
    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val date: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.image)

    }
}




