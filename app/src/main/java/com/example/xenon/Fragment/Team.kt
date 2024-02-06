package com.example.xenon.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.Team.WingAdapter
import com.example.xenon.DataClass.Team.TeamMember
import com.example.xenon.DataClass.Team.TeamSection
import com.example.xenon.R
import com.example.xenon.databinding.FragmentTeamBinding
import com.google.firebase.firestore.FirebaseFirestore

class Team : Fragment() {
    private lateinit var binding: FragmentTeamBinding
    private var teamSections: MutableList<TeamSection> = mutableListOf()
    private lateinit var wingAdapter: WingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamBinding.inflate(layoutInflater, container, false)
        binding.teamRV.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wingAdapter = WingAdapter(requireContext(), teamSections)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())
        binding.imageView4.setOnClickListener {
            loadFragment(AboutUs())
        }

        binding.menu.setOnClickListener {
            openDrawer()
        }
        fetchFromFirestore()
        fetch()
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
    private fun fetch() {
        val sharedPreferences =
            requireContext().getSharedPreferences("ImageData", Context.MODE_PRIVATE)
        val imageUrl = sharedPreferences.getString("imageUrl", "")

        if (imageUrl.isNullOrEmpty()) {
            val db = FirebaseFirestore.getInstance()
            db.collection("Meet").get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.getString("img") ?: ""
                    Glide.with(requireContext())
                        .load(img)
                        .error(R.drawable.group)
                        .placeholder(R.drawable.placeholder)
                        .transform(CenterCrop(), RoundedCorners(20))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.imageView4)

                    val editor = sharedPreferences.edit()
                    editor.putString("imageUrl", img)
                    editor.apply()
                }
            }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Glide.with(requireContext())
                .load(imageUrl)
                .transform(CenterCrop(), RoundedCorners(20))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imageView4)
        }
    }

    private fun fetchFromFirestore() {
        teamSections.clear()
        val db = FirebaseFirestore.getInstance()
        val teamCollection = db.collection("Team").orderBy("no")
        teamCollection.get().addOnSuccessListener { documents ->
            val wingMap = mutableMapOf<String, MutableList<TeamMember>>()
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val img = document.getString("image") ?: ""
                val role = document.getString("role") ?: ""
                val wing = document.getString("wing") ?: ""
                val teamMember = TeamMember(name, img)
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
            binding.resLot.visibility = View.INVISIBLE
            binding.teamRV.visibility = View.VISIBLE
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