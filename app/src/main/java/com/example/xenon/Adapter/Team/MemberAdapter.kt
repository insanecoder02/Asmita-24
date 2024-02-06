package com.example.xenon.Adapter.Team

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.xenon.DataClass.Team.TeamMember
import com.example.xenon.R
import com.example.xenon.other.ImageViewerDialog
import kotlin.math.roundToInt

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
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(holder.img)

        holder.img.setOnClickListener {
            val dialog = ImageViewerDialog(holder.itemView.context, teamMember.img)
            dialog.show()
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

    override fun getItemCount(): Int = members.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val img:ImageView = itemView.findViewById(R.id.mem_img)
    }
}