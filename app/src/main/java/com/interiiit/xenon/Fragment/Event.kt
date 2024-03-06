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
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.interiiit.xenon.Adapter.Team.EventsAdapter
import com.interiiit.xenon.DataClass.EventDataClass.EveDataClass
import com.interiiit.xenon.DataClass.EventDataClass.Events
import com.interiiit.xenon.databinding.FragmentEventBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interiiit.xenon.Activity.Main

class Event : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private var eventClass: MutableList<EveDataClass> = mutableListOf()
    private lateinit var wingAdapter: EventsAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(layoutInflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("Event", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wingAdapter = EventsAdapter(requireContext(), eventClass)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())

        binding.back.setOnClickListener {
            openDrawer()
        }
        binding.refresh.setOnRefreshListener {
            fetchFromFirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        fetchIfNeeded()
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

    private fun fetchIfNeeded() {
        if (shouldFetchData()) {
            binding.resLot.visibility = View.VISIBLE
            binding.teamRV.visibility = View.INVISIBLE
            fetchFromFirestore()
        } else {
            loadFromCache()
        }
    }

    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastEveFetchTime", 0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastFetchTime
        val fetchInterval = 24*60*60*1000
        return !sharedPreferences.getBoolean(
            "eveDataFetched",
            false
        ) || elapsedTime >= fetchInterval
    }

    private fun loadFromCache() {
        val json = sharedPreferences.getString("cachedEveData", null)
        if (json != null) {
            val type = object : TypeToken<List<EveDataClass>>() {}.type
            val eveList: List<EveDataClass> = Gson().fromJson(json, type)
            eventClass.clear()
            eventClass.addAll(eveList)
            wingAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.teamRV.visibility = View.VISIBLE
        }
    }

    private fun fetchFromFirestore() {
        eventClass.clear()
        val url = "https://app-admin-api.asmitaiiita.org/api/events"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val eveMap = mutableMapOf<String, MutableList<Events>>()
                val documents = response.getJSONArray("data")
                for (i in 0 until documents.length()) {
                    val document = documents.getJSONObject(i)
                    val name = document.getString("name") ?: ""
                    val image = document.getString("image") ?: ""
                    val wing = document.getString("wing") ?: ""
                    val event = Events(name, image, wing)
                    if (eveMap.containsKey(wing)) {
                        eveMap[wing]?.add(event)
                    } else {
                        eveMap[wing] = mutableListOf(event)
                    }
                }
                for ((wing, members) in eveMap) {
                    val eveSection = EveDataClass(wing, members)
                    eventClass.add(eveSection)
                }
                wingAdapter.notifyDataSetChanged()

                if (eventClass.isEmpty()) {
                    // Handle case when there are no events
                    binding.t1.visibility = View.VISIBLE
                    binding.resLot.visibility = View.INVISIBLE
                    binding.normal.visibility = View.INVISIBLE
                    binding.error.visibility = View.INVISIBLE
                    binding.refresh.isRefreshing = false
                } else {
                    // Handle case when there are events
                    binding.t1.visibility = View.INVISIBLE
                    binding.resLot.visibility = View.INVISIBLE
                    binding.refresh.isRefreshing = false
                    binding.teamRV.visibility = View.VISIBLE
                    binding.normal.visibility = View.VISIBLE
                    binding.error.visibility = View.INVISIBLE
                    updateSharedPreferences()
                }
            },
            { error ->
                handleNetworkError()
                Toast.makeText(requireContext(), "Network Error", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(requireContext()).add(request)
    }


    private fun handleNetworkError() {
        binding.normal.visibility = View.INVISIBLE
        binding.error.visibility = View.VISIBLE
        binding.refresh.isRefreshing = false
        binding.loadBtn.setOnClickListener {
            fetchFromFirestore()
            binding.refresh.isRefreshing = true
        }
    }

    private fun updateSharedPreferences() {
        val json = Gson().toJson(eventClass)
        sharedPreferences.edit().apply {
            putBoolean("eveDataFetched", true)
            putLong("lastEveFetchTime", System.currentTimeMillis())
            putString("cachedEveData", json)
            apply()
        }
    }
}




