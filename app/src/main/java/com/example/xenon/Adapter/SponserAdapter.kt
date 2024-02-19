package com.example.xenon.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.DataClass.Sponser
import com.example.xenon.R
import kotlin.random.Random

class SponserAdapter(
    private val spons: MutableList<Sponser>
) : RecyclerView.Adapter<SponserAdapter.ViewHolder>() {
    private var isAnimating: Boolean = false
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name: TextView = itemView.findViewById(R.id.sponsText)
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

        holder.itemView.setOnClickListener { itemView ->
                itemView.isClickable = false // Disable click on the clicked item

                val rotationDirection = if (Random.nextBoolean()) 1 else -1
                itemView.animate().rotationYBy(rotationDirection * 360f).setDuration(1000)
                    .withEndAction {
                        itemView.isClickable = true // Enable click once animation is completed
                    }.start()
        }

//        holder.name.text = spo.name
    }
}