package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xenon.DataClass.Events
import com.example.xenon.R

class EventsAdapter(
    val context: Context,
    val datalist: MutableList<Events>
):RecyclerView.Adapter<EventsAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sports_item_view,parent,false)
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
                .error(R.drawable.group)
                 .into(holder.image)
        }
    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.name)
        val date: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.image)

    }

    }


