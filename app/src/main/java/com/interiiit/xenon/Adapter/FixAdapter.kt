package com.interiiit.xenon.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.DataClass.FixtureDataClass.FixtureSportDataClass
import com.interiiit.xenon.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

class FixAdapter(
    private val day: MutableList<FixtureSportDataClass>,
    private val itemClickListener: com.interiiit.xenon.Fragment.Home
) : RecyclerView.Adapter<FixAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val time:TextView = itemView.findViewById(R.id.time)
        val date:TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return day.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fix = day[position]
        holder.name.text = fix.type
        val timeC = fix.fix
            val createdAt = timeC[0].time
            holder.time.text = getTimeAgo(createdAt)
        holder.date.text = timeC[0].day
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(day[position])
        }
    }

    fun getDate(createdAt: String):String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date: Date = sdf.parse(createdAt)
        val formattedDate = SimpleDateFormat("dd-MM-yyyy").format(date)
        return formattedDate
    }
    fun getTimeAgo(createdAt: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = OffsetDateTime.parse(createdAt, formatter)
        val istDateTime = dateTime.withOffsetSameInstant(ZoneOffset.ofHoursMinutes(5, 30)) // Convert to IST

        val now = OffsetDateTime.now()
        val duration = Duration.between(istDateTime, now)
        val seconds = duration.seconds

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date: Date = sdf.parse(createdAt)
        val formattedDate = SimpleDateFormat("dd-MM-yyyy").format(date)


        return when {
            seconds < 60 -> "Just now"
            seconds < 60 * 60 -> "${seconds / 60} minutes ago"
            seconds < 60 * 60 * 24 -> "${seconds / (60 * 60)} hours ago"
            seconds < 60 * 60 * 24 * 30 -> "${seconds / (60 * 60 * 24)} days ago"
            seconds < 60 * 60 * 24 * 30 * 12 -> "${seconds / (60 * 60 * 24 * 30)} months ago"
            else -> "${seconds / (60 * 60 * 24 * 30 * 12)} years ago"
        }
    }
}