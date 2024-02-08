package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.Adapter.EventsAdapter.FeaturedEventsAdapter
import com.example.xenon.Adapter.Team.EventsAdapter
import com.example.xenon.DataClass.EveDataClass
import com.example.xenon.DataClass.Events
import com.example.xenon.R
import com.example.xenon.databinding.FragmentEventBinding
import com.example.xenon.other.AutoScroll
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class Event : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private var eventClass: MutableList<EveDataClass> = mutableListOf()
    private lateinit var wingAdapter: EventsAdapter
    private var featuredClass: MutableList<Events> = mutableListOf()
    private lateinit var eventsAdapter: FeaturedEventsAdapter
    private val autoScrollManagers = mutableListOf<AutoScroll>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(layoutInflater, container, false)
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wingAdapter = EventsAdapter(requireContext(), eventClass)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())

        eventsAdapter = FeaturedEventsAdapter(requireContext(), featuredClass, this)
        binding.featuredRv.adapter = eventsAdapter
        binding.featuredRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        fetchFromFirestore()
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.refresh.setOnRefreshListener {
            fetchFromFirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }

        rotor(binding.featuredRv)
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

            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
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




