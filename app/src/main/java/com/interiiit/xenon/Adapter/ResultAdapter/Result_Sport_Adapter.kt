package com.interiiit.xenon.Adapter.ResultAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.DataClass.Score.Matches
import com.interiiit.xenon.R

class Result_Sport_Adapter (val sch: List<Matches>,
                            private val itemClickListener: com.interiiit.xenon.Fragment.Result_Sport
) : RecyclerView.Adapter<Result_Sport_Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_fix, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = sch.size
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.dayText)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scc = sch[position]
        holder.name.text = scc.type
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(sch[position])
        }
    }
}