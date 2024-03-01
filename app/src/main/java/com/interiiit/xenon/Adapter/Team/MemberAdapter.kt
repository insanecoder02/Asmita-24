package com.interiiit.xenon.Adapter.Team

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.interiiit.xenon.DataClass.Team.TeamMember
import com.interiiit.xenon.R
import com.interiiit.xenon.other.ImageViewerDialog

class MemberAdapter(private val members:List<TeamMember>) :
    RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_member, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teamMember = members[position]
        holder.name.text = teamMember.name

        Glide.with(holder.itemView.context)
            .load(teamMember.img)
            .thumbnail(0.1f)
            .transform(CircleCrop())
            .error(R.drawable.group)
            .placeholder(R.drawable.circular_bg)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.img)

        holder.img.setOnClickListener {
            val dialog = ImageViewerDialog(holder.itemView.context, teamMember.img)
            dialog.show()
        }
    }

    override fun getItemCount(): Int = members.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val img:ImageView = itemView.findViewById(R.id.mem_img)
    }
}