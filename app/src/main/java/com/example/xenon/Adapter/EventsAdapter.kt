package com.example.xenon.Adapter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xenon.DataClass.Events
import com.example.xenon.DataClass.Gallery2
import com.example.xenon.Fragment.Gallery
import com.example.xenon.R

class EventsAdapter(
    val context: Context,
    val datalist: MutableList<Events>,
    private val itemClickListener: com.example.xenon.Fragment.Event
):RecyclerView.Adapter<EventsAdapter.viewHolder>() {

//    private val handler = Handler(Looper.getMainLooper())
//    private val delay: Long = 2000 // 2 second delay between item scrolls
//    private var currentItemPosition = 0
//    private lateinit var recyclerView: RecyclerView


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

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(datalist[position])
        }


        }
    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.name)
        val date: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.image)

    }

//    fun startAutoScroll(recyclerView: RecyclerView) {
//        this.recyclerView = recyclerView
//        handler.postDelayed(scrollRunnable, delay)
//    }
//
//    private val scrollRunnable = object : Runnable {
//        override fun run() {
//            if (currentItemPosition < datalist.size - 1) {
//                currentItemPosition++
//            } else {
//                currentItemPosition = 0
//            }
//            recyclerView.smoothScrollToPosition(currentItemPosition)
//            handler.postDelayed(this, delay)
//        }
//    }

    }




