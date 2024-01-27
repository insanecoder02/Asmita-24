package com.example.xenon.Adapter.Score

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.xenon.DataClass.Score.MatchDetails
import com.example.xenon.R
import org.w3c.dom.Text

/*

this page is going to  have both the code of liveMatches adapter , upcommignMatchesAdpater

*/

class UpcommingMatchAdapter(
    private val sch: List<MatchDetails>
) : RecyclerView.Adapter<UpcommingMatchAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_schedule, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int = sch.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val scc = sch[position]

        holder.matName.text = scc.matchName
        holder.time.text = scc.time
        holder.name1.text = scc.clgName1
        holder.name2.text = scc.clgName2
        holder.date.text = scc.date

        Glide.with(holder.itemView.context)
            .load(scc.clgImg1)
            .placeholder(R.drawable.iiita) // Replace with your placeholder drawable
            .error(R.drawable.iiita) // Replace with your error drawable
            .into(holder.img1)

        Glide.with(holder.itemView.context)
            .load(scc.clgImg2)
            .placeholder(R.drawable.iiita) // Replace with your placeholder drawable
            .error(R.drawable.iiita) // Replace with your error drawable
            .into(holder.img2)


        if (scc.live == "y") {
            holder.live.visibility = View.VISIBLE
            holder.live.loop(true)
            holder.live.playAnimation()
        }

    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matName: TextView = itemView.findViewById(R.id.matchName)
        val live: LottieAnimationView = itemView.findViewById(R.id.liveLottie)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.time)
        val name2: TextView = itemView.findViewById(R.id.clgName2)
        val name1: TextView = itemView.findViewById(R.id.clgName1)
        val img1: ImageView = itemView.findViewById(R.id.clgImg1)
        val img2: ImageView = itemView.findViewById(R.id.clgImg2)
    }
}

class LiveMatchAdapter(
    private val lll: List<MatchDetails>
) : RecyclerView.Adapter<LiveMatchAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.live_match_rv, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int = lll.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val scc = lll[position]

        holder.matName.text = scc.matchName
        holder.name1.text = scc.clgName1
        holder.name2.text = scc.clgName2
        holder.score1.text = scc.score1
        holder.score2.text = scc.score2

        Glide.with(holder.itemView.context)
            .load(scc.clgImg1)
            .placeholder(R.drawable.iiita) // Replace with your placeholder drawable
            .error(R.drawable.iiita) // Replace with your error drawable
            .into(holder.img1)

        Glide.with(holder.itemView.context)
            .load(scc.clgImg2)
            .placeholder(R.drawable.iiita) // Replace with your placeholder drawable
            .error(R.drawable.iiita) // Replace with your error drawable
            .into(holder.img2)


        if (scc.live == "y") {
            holder.live.visibility = View.VISIBLE
            holder.live.loop(true)
            holder.live.playAnimation()
        }

    }

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matName: TextView = itemView.findViewById(R.id.matchNameLiveMatch)
        val live: LottieAnimationView = itemView.findViewById(R.id.liveLottieLiveMatch)
        val name2: TextView = itemView.findViewById(R.id.clgName2LiveMatch)
        val score1: TextView  = itemView.findViewById(R.id.clg1scoreLiveMatch)
        val score2: TextView = itemView.findViewById(R.id.clg2scrLiveMatch)
        val name1: TextView = itemView.findViewById(R.id.clgName1LiveMatch)
        val img1: ImageView = itemView.findViewById(R.id.clgImg1LiveMatch)
        val img2: ImageView = itemView.findViewById(R.id.clgImg2LiveMatch)
    }
}




