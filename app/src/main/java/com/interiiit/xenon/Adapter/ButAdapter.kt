package com.interiiit.xenon.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.R

class ButAdapter(
    private val butt: List<Fixture_Day_DataClass>,
    private val itemClickListener: com.interiiit.xenon.Fragment.Fixture_Day_Wise
) : RecyclerView.Adapter<ButAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: AppCompatButton = itemView.findViewById(R.id.day)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_butt, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return butt.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bt = butt[position]
        holder.day.text = bt.day

        holder.itemView.setOnClickListener {
            itemClickListener.onButClick(butt[position])
            setSelectedPosition(position)
        }

        if (position == selectedItemPosition) {
            holder.day.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E9BD3E"))
            // Display the value related to the selected item
            itemClickListener.onButClick(butt[position])
        } else {
            holder.day.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    // Method to get the selected item position
    fun getSelectedPosition(): Int {
        return selectedItemPosition
    }
}