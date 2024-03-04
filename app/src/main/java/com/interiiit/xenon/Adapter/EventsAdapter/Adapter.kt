package com.interiiit.xenon.Adapter.Team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.interiiit.xenon.DataClass.EventDataClass.Events
import com.interiiit.xenon.Fragment.sport_detail
import com.interiiit.xenon.R

class Adapter(private val fragmentManager: FragmentManager,
              private val evee:List<Events>
) :RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_eve, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = evee[position]
        holder.name.text = event.name

        Glide.with(holder.itemView.context)
            .load(event.image)
            .thumbnail(0.1f)
            .fitCenter()
            .transform(RoundedCorners(20))
            .error(R.drawable.group)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", event.name ?: "Name")
                putString("date", event.date ?: "Date")
                putString("image", event.image ?: "image")
                putString("discription", event.discription ?: "Discription")
                putString("heading", event.heading ?: "Heading")
                putString("length", event.length ?: "Length")
                putString("location", event.location ?: "Location")
                putString("type", event.type ?: "Type")
            }
            val fragment = sport_detail().apply {
                arguments = bundle
            }
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int = evee.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val img:ImageView = itemView.findViewById(R.id.mem_img)
    }
}