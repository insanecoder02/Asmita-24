package com.interiiit.xenon.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.interiiit.xenon.DataClass.DeveloperDataClass
import com.interiiit.xenon.R

class DevAdapter(private val dev: MutableList<DeveloperDataClass>) :
    RecyclerView.Adapter<DevAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val desig: TextView = itemView.findViewById(R.id.desginations)
        val img: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_developer, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dev.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val devel = dev[position]

        holder.name.text = devel.name
        holder.desig.text = devel.pos
        Glide.with(holder.itemView.context)
            .load(devel.img)
            .transform(RoundedCorners(10))
            .error(R.drawable.group)
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.img)

        if(devel.link != ""){
            holder.itemView.setOnClickListener {
                val openUrl = Intent(Intent.ACTION_VIEW)
                openUrl.data = Uri.parse(devel.link)
                holder.itemView.context.startActivity(openUrl)
            }
        }
        else{
            Toast.makeText(holder.itemView.context, "Link Not Found", Toast.LENGTH_SHORT).show()
        }
    }
}
