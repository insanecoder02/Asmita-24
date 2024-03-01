package com.interiiit.xenon.Adapter.Score

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.interiiit.xenon.DataClass.Score.MatchDetails
import com.interiiit.xenon.Fragment.Fixture_Sport_Wise
import com.interiiit.xenon.R

class UpcomingMatchAdapter(
    private val sch: List<MatchDetails>,
    private val fragmentManager: FragmentManager,
    private val isHomeFragment: Boolean
) : RecyclerView.Adapter<UpcomingMatchAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_schedule, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int = sch.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val scc = sch[position]

        holder.matName.text = scc.matchName
        holder.itemView.setOnClickListener {
            if(isHomeFragment){
                load()
            }
        }
        holder.name1.text = scc.clgName1
        holder.name2.text = scc.clgName2
        holder.date.text = scc.date
//        holder.time.text = scc.time

        Glide.with(holder.itemView.context)
            .load(scc.clgImg1)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.group) // Replace with your error drawable
            .into(holder.img1)

        Glide.with(holder.itemView.context)
            .load(scc.clgImg2)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.group)
            .into(holder.img2)
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name:TextView = itemView.findViewById(R.id.name)
        val matName: TextView = itemView.findViewById(R.id.matchName)
        val date: TextView = itemView.findViewById(R.id.date)
//        val time: TextView = itemView.findViewById(R.id.grp)
        val name2: TextView = itemView.findViewById(R.id.clgName2)
        val name1: TextView = itemView.findViewById(R.id.clgName1)
        val img1: ImageView = itemView.findViewById(R.id.clgImg1)
        val img2: ImageView = itemView.findViewById(R.id.clgImg2)
    }
    private fun load(){
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Fixture_Sport_Wise())
            .addToBackStack(null)
            .commit()
    }
}



