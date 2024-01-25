package com.example.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.DataClass.Message
import com.example.xenon.R

class NotificationAdapter(private val msg: List<Message>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tit: TextView = itemView.findViewById(R.id.title)
        val des: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_notification, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return msg.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mesg = msg[position]
        holder.tit.text = mesg.msg
        holder.des.text = mesg.desc
    }
}