package com.example.xenon.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.Team.WingAdapter
import com.example.xenon.DataClass.Team.TeamMember
import com.example.xenon.DataClass.Team.TeamSection
import com.example.xenon.databinding.FragmentTeamBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson

class Team : Fragment() {
    private lateinit var binding: FragmentTeamBinding
    private var teamSections: MutableList<TeamSection> = mutableListOf()
    private lateinit var wingAdapter: WingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wingAdapter = WingAdapter(requireContext(), teamSections)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())
        fetchFromFirestore()
    }

//    private fun loadData() {
//        // Try to fetch data from SharedPreferences
//        val sharedPreferences = requireContext().getSharedPreferences("TeamData", Context.MODE_PRIVATE)
//        val savedDataJson = sharedPreferences.getString("teamSections", null)
//
//        if (savedDataJson != null) {
//            // Data exists in SharedPreferences, load and display
//            val savedData = Gson().fromJson(savedDataJson, TeamData::class.java)
//            teamSections.addAll(savedData.teamSections)
//            wingAdapter.notifyDataSetChanged()
//        }
//
//        // Fetch data from Firestore
//        fetchFromFirestore()
//    }

    private fun fetchFromFirestore() {
        teamSections.clear()
        val db = FirebaseFirestore.getInstance()
        val teamCollection = db.collection("Team").orderBy("no")
        teamCollection.get().addOnSuccessListener { documents ->
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
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }


//    private fun updateLocalCache(snapshots: QuerySnapshot) {
//        teamSections.clear()
//        val wingMap = mutableMapOf<String, MutableList<TeamMember>>()
//        for (document in snapshots.documents) {
//            // Process Firestore document and update local cache
//            val name = document.getString("name") ?: ""
//            val role = document.getString("role") ?: ""
//            val wing = document.getString("wing") ?: ""
//            val teamMember = TeamMember(name)
//            if (wingMap.containsKey(wing)) {
//                wingMap[wing]?.add(teamMember)
//            } else {
//                wingMap[wing] = mutableListOf(teamMember)
//            }
//        }
//        for ((wing, members) in wingMap) {
//            val teamSection = TeamSection(wing, members)
//            teamSections.add(teamSection)
//        }
//        wingAdapter.notifyDataSetChanged()
//
//        // Save the updated data to SharedPreferences
//        saveDataToSharedPreferences()
//    }

//    .get().addOnSuccessListener { documents ->
//            val wingMap = mutableMapOf<String, MutableList<TeamMember>>()
//            for (document in documents) {
//                val name = document.getString("name") ?: ""
//                val role = document.getString("role") ?: ""
//                val wing = document.getString("wing") ?: ""
//                val teamMember = TeamMember(name)
//                if (wingMap.containsKey(wing)) {
//                    wingMap[wing]?.add(teamMember)
//                } else {
//                    wingMap[wing] = mutableListOf(teamMember)
//                }
//            }
//            for ((wing, members) in wingMap) {
//                val teamSection = TeamSection(wing, members)
//                teamSections.add(teamSection)
//            }
//            wingAdapter.notifyDataSetChanged()
//            saveDataToSharedPreferences()
//        }.addOnFailureListener { exception ->
//            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun saveDataToSharedPreferences() {
//        val sharedPreferences = requireContext().getSharedPreferences("TeamData", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        val teamData = TeamData(teamSections.toList())
//        val dataJson = Gson().toJson(teamData)
//
//        editor.putString("teamSections", dataJson)
//        editor.apply()
//    }
//    data class TeamData(val teamSections: List<TeamSection>)
}