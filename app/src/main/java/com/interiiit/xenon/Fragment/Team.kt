package com.interiiit.xenon.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
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
import org.json.JSONException

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
            binding.refresh.isEnabled = scrollY == 0
        }

        sharedPreferences = requireActivity().getSharedPreferences("Teams", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext())
            .load(R.drawable.teamss)
            .placeholder(R.drawable.rectangle_bg)
            .transform(CenterCrop(), RoundedCorners(20))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.imageView4)
        wingAdapter = WingAdapter(requireContext(), teamSections)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())
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
        return !sharedPreferences.getBoolean(
            "teamDataFetched",
            false
        ) || elapsedTime >= fetchInterval
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

    private fun fetchFromFirestore() {
        teamSections.clear()
        val url = "https://app-admin-api.asmitaiiita.org/api/teamAsmita"
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val wingMap = mutableMapOf<String, MutableList<TeamMember>>()
                    val documents = response.getJSONArray("data")
                    for (i in 0 until documents.length()) {
                        val document = documents.getJSONObject(i)
                        val name = document.getString("name")?:""
                        val img = document.getString("img_url")?:""
                        val wing = document.getString("wing")?:""
                        val teamMember = TeamMember(name, img)
                        Log.d("hello",TeamMember(name, img).toString())
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
//                    if (teamSections.isEmpty()) {
                        binding.resLot.visibility = View.INVISIBLE
                        binding.teamRV.visibility = View.VISIBLE
                        binding.refresh.isRefreshing = false
                        binding.normal.visibility = View.VISIBLE
                        binding.error.visibility = View.INVISIBLE
//                    } else {
//
//                    }
                    updateSharedPreferences()
                } catch (e: JSONException) {
                    handleNetworkError()
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()

                }
            },
            { error ->
                handleNetworkError()
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonObjectRequest)
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