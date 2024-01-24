package com.example.xenon.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.Team.WingAdapter
import com.example.xenon.DataClass.Team.TeamMember
import com.example.xenon.DataClass.Team.TeamSection
import com.example.xenon.R
import com.example.xenon.databinding.ActivityEvents1Binding
import com.google.firebase.firestore.FirebaseFirestore


class Events_1 : AppCompatActivity() {
    private lateinit var binding: ActivityEvents1Binding
    private var teamSections: MutableList<TeamSection> = mutableListOf()
    private lateinit var wingAdapter: WingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEvents1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        wingAdapter = WingAdapter(this, teamSections)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(this)
        fetchFromFirestore()
    }
    private fun fetchFromFirestore() {
        teamSections.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Event").get().addOnSuccessListener { documents ->
            val wingMap = mutableMapOf<String, MutableList<TeamMember>>()
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val role = document.getString("role") ?: ""
                val wing = document.getString("wing") ?: ""
                val teamMember = TeamMember(name)
                if (wingMap.containsKey(wing)) {
                    wingMap[wing]?.add(teamMember)
                } else {
                    wingMap[wing] = mutableListOf(teamMember)
                }
            }
            for ((wing, members) in wingMap) {
                val teamSection = TeamSection(wing, members)
                teamSections.add(teamSection)
            }
            wingAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}