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
import androidx.recyclerview.widget.RecyclerView
import com.interiiit.xenon.Adapter.EventsAdapter.FeaturedEventsAdapter
import com.interiiit.xenon.Adapter.Team.EventsAdapter
import com.interiiit.xenon.DataClass.EventDataClass.EveDataClass
import com.interiiit.xenon.DataClass.EventDataClass.Events
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentEventBinding
import com.interiiit.xenon.other.AutoScroll
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Event : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private var eventClass: MutableList<EveDataClass> = mutableListOf()
    private lateinit var wingAdapter: EventsAdapter
    private var featuredClass: MutableList<Events> = mutableListOf()
    private lateinit var eventsAdapter: FeaturedEventsAdapter
    private val autoScrollManagers = mutableListOf<AutoScroll>()
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

        wingAdapter = EventsAdapter(requireContext(), eventClass,parentFragmentManager)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())

        eventsAdapter = FeaturedEventsAdapter(requireContext(), featuredClass, this)
        binding.featuredRv.adapter = eventsAdapter
        binding.featuredRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.refresh.setOnRefreshListener {
            fetchFromFirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
//        fetchIfNeeded()
        fetchFromFirestore()
        rotor(binding.featuredRv)
    }
    private fun fetchIfNeeded() {
//        if (shouldFetchData()) {
//            binding.resLot.visibility = View.VISIBLE
//            fetchFromFirestore()
//        } else {
//            loadFromCache()
//        }
    }
    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastEveFetchTime", 0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastFetchTime
        val fetchInterval = 1 * 60 * 1000 // 1 hours in milliseconds

        // Check if data has never been fetched or if more than 24 hours have passed since last fetch
        return !sharedPreferences.getBoolean("eveDataFetched", false) || elapsedTime >= fetchInterval
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
        }
    }

    private fun rotor(recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val autoScroll = AutoScroll(recyclerView)
        autoScroll.startAutoScroll()
        autoScrollManagers.add(autoScroll)
    }

    private fun fetchFromFirestore() {
        featuredClass.clear()
        eventClass.clear()
        FirebaseFirestore.getInstance().collection("Event").get()
            .addOnSuccessListener { documents ->
                val eveMap = mutableMapOf<String, MutableList<Events>>()
                for (document in documents) {
                    val name = document.getString("name") ?: ""
                    val image = document.getString("image") ?: ""
                    val date = document.getString("date") ?: ""
                    val discription = document.getString("description") ?: ""
                    val heading = document.getString("heading") ?: ""
                    val length = document.getString("length") ?: ""
                    val location = document.getString("location") ?: ""
                    val type = document.getString("type") ?: ""
                    val wing = document.getString("wing") ?: ""
                    val feat = document.getString("feat")?:""
                    val event =
                        Events(name, date, image, discription, heading, length, location, type,wing,feat)
                    if (eveMap.containsKey(wing)) {
                        eveMap[wing]?.add(event)
                    } else {
                        eveMap[wing] = mutableListOf(event)
                    }
                    if (feat == "y") {
                        featuredClass.add(event)
                    }
                }
                for ((wing, members) in eveMap) {
                    val eveSection = EveDataClass(wing, members)
                    eventClass.add(eveSection)
                }
                eventsAdapter.notifyDataSetChanged()
                wingAdapter.notifyDataSetChanged()
                binding.resLot.visibility = View.INVISIBLE
                binding.refresh.isRefreshing=false
                binding.normal.visibility = View.VISIBLE
                binding.error.visibility = View.INVISIBLE
//                updateSharedPreferences()
            }.addOnFailureListener { exception ->
                handleNetworkError()
//                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT)
//                    .show()
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
        val json = Gson().toJson(eventClass)
        sharedPreferences.edit().apply {
            putBoolean("eveDataFetched", true)
            putLong("lastEveFetchTime", System.currentTimeMillis())
            putString("cachedEveData", json)
            apply()
        }
    }

    fun onItemClick(item: Events) {
        val bundle = Bundle()
        bundle.putString("name", item.name ?: "Name")
        bundle.putString("date", item.date ?: "Date")
        bundle.putString("image", item.image ?: "image")
        bundle.putString("discription", item.discription ?: "Discription")
        bundle.putString("heading", item.heading ?: "Heading")
        bundle.putString("length", item.length ?: "Length")
        bundle.putString("location", item.location ?: "Location")
        bundle.putString("type", item.type ?: "Type")
        val nextFragment = sport_detail()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}




