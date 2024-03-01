package com.interiiit.xenon.Adapter.Team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.DataClass.EventDataClass.EveDataClass
import com.interiiit.xenon.R

class EventsAdapter(private val context: Context, private val eve: MutableList<EveDataClass>, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<EventsAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_eve1, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val eveSection = eve[position]
        holder.eveName.text = eveSection.eveType
        holder.eveRv.adapter = Adapter(fragmentManager,eveSection.eve)
        holder.eveRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getItemCount(): Int = eve.size

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eveName: TextView = itemView.findViewById(R.id.wingName)
        val eveRv: RecyclerView = itemView.findViewById(R.id.membersRv)
    }
}
