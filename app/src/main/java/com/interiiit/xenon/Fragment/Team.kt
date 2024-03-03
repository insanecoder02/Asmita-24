package com.interiiit.xenon.Fragment

import android.content.Context
import android.content.SharedPreferences
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
import com.interiiit.xenon.Activity.Main
import com.interiiit.xenon.Adapter.Team.WingAdapter
import com.interiiit.xenon.DataClass.Team.TeamMember
import com.interiiit.xenon.DataClass.Team.TeamSection
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentTeamBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Team : Fragment() {
    private lateinit var binding: FragmentTeamBinding
    private var teamSections: MutableList<TeamSection> = mutableListOf()
    private lateinit var wingAdapter: WingAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamBinding.inflate(layoutInflater, container, false)
        binding.refresh.isEnabled = false

        binding.normal.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            // Enable or disable swipe refresh based on scroll position
            binding.refresh.isEnabled = scrollY == 0
        }
        
        sharedPreferences = requireActivity().getSharedPreferences("Team", Context.MODE_PRIVATE)
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
        binding.refresh.setOnRefreshListener {
            fetchFromFirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        binding.menu.setOnClickListener {
            openDrawer()
        }
        fetchIfNeeded()
    }
    private fun fetchIfNeeded() {
        if (shouldFetchData()) {
            binding.teamRV.visibility = View.INVISIBLE
            binding.resLot.visibility = View.VISIBLE
            fetchFromFirestore()
        } else {
            loadFromCache()
        }
    }

    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastTeamFetchTime", 0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastFetchTime
        val fetchInterval = 24 * 60 * 60 * 1000
        return !sharedPreferences.getBoolean("teamDataFetched", false) || elapsedTime >= fetchInterval
    }
    private fun loadFromCache() {
        val json = sharedPreferences.getString("cachedTeamData", null)
        if (json != null) {
            val type = object : TypeToken<List<TeamSection>>() {}.type
            val partList: List<TeamSection> = Gson().fromJson(json, type)
            teamSections.clear()
            teamSections.addAll(partList)
            wingAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.teamRV.visibility = View.VISIBLE
        }
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
    private fun fetchFromFirestore() {
        teamSections.clear()
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
            }
        }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
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
            binding.refresh.isRefreshing = false
            binding.normal.visibility = View.VISIBLE
            binding.error.visibility = View.INVISIBLE
            updateSharedPreferences()
        }.addOnFailureListener { exception ->
            handleNetworkError()
//            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleNetworkError() {
        binding.normal.visibility = View.INVISIBLE
        binding.error.visibility = View.VISIBLE
        binding.refresh.isRefreshing = false
        binding.loadBtn.setOnClickListener {
            fetchFromFirestore()
        }
    }
    private fun updateSharedPreferences() {
        val json = Gson().toJson(teamSections)
        sharedPreferences.edit().apply {
            putBoolean("teamDataFetched", true)
            putLong("lastTeamFetchTime", System.currentTimeMillis())
            putString("cachedTeamData", json)
            apply()
        }
    }
}