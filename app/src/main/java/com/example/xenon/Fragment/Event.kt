package com.example.xenon.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Activity.GetStarted
import com.example.xenon.Adapter.Team.WingAdapter
import com.example.xenon.DataClass.Team.TeamMember
import com.example.xenon.DataClass.Team.TeamSection
import com.example.xenon.Adapter.EventsAdapter
import com.example.xenon.DataClass.Events
import com.example.xenon.R
import com.example.xenon.databinding.FragmentEventBinding
import com.google.firebase.firestore.FirebaseFirestore

class Event : Fragment() {


    private lateinit var binding: FragmentEventBinding
    private var teamSections: MutableList<TeamSection> = mutableListOf()
    private lateinit var wingAdapter: WingAdapter

    //for Events Adapter

    private var sportSections : MutableList<Events> = mutableListOf()
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wingAdapter = WingAdapter(requireContext(), teamSections)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())

        //for Events Adpater
        eventsAdapter = EventsAdapter(requireContext(),sportSections,this)
        binding.sports.adapter = eventsAdapter
        binding.sports.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
       // eventsAdapter.startAutoScroll(binding.sports)

        fetchFromFirestore1()
        fetchFromFirestore2()


    }

    private fun fetchFromFirestore1() {
        teamSections.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Event").get().addOnSuccessListener { documents ->
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
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromFirestore2() {
        sportSections.clear()
        FirebaseFirestore.getInstance().collection("FeaturedEvents").get()
            .addOnSuccessListener{documents->
                for(document in  documents){
                    val name = document.getString("name") ?: ""
                    val image = document.getString("image") ?: ""
                    val date = document.getString("date") ?: ""
                    val discription = document.getString("discription") ?: ""
                    val heading = document.getString("heading") ?: ""
                    val length = document.getString("length") ?:""
                    val location = document.getString("location") ?:""
                    val type = document.getString("type") ?:""



                    sportSections.add(Events(name,date,image,discription,heading,length,location,type))
                }
                eventsAdapter.notifyDataSetChanged()
            }.addOnFailureListener{exception->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_SHORT).show()
            }

    }

    fun onItemClick(item: Events){

        val bundle = Bundle()
        bundle.putString("name",item.name ?: "Name")
        bundle.putString("date",item.date ?: "Date")
        bundle.putString("image",item.image ?: "image")
        bundle.putString("discription",item.discription ?: "Discription")
        bundle.putString("heading",item.heading ?: "Heading")
        bundle.putString("length",item.length ?: "Length")
        bundle.putString("location",item.location ?: "Location")
        bundle.putString("type",item.type ?: "Type")
        val nextFragment = Gallery()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragment_container, nextFragment
        )
        transaction.addToBackStack(null)
        transaction.commit()

    }
}






//fun onItemClick(item: Gallery2) {
////        Log.d(
////            "Events",
////            "Date: ${item.date}, Details: ${item.details}, Form: ${item.form}, Name: ${item.name}, No: ${item.no}, Time: ${item.time}, URL: ${item.url}, Venue: ${item.venue}"
////        )
//    val bundle = Bundle()
//    bundle.putString("date", item.sport_img ?: "Date")
//    bundle.putString("details", item.sport_name)
////        bundle.putString("form", item.form ?: "Form")
////        bundle.putString("name", item.name ?: "Name")
////        bundle.putLong("no", item.no ?: 123)
////        bundle.putString("time", item.time ?: "Time")
////        bundle.putString("url", item.url ?: "Url")
////        bundle.putString("venue", item.venue ?: "Venue")
//    val nextFragment = Gallery()
//    nextFragment.arguments = bundle
//    val transaction = requireActivity().supportFragmentManager.beginTransaction()
//    transaction.replace(
//        R.id.fragment_container, nextFragment
//    ) // Use nextFragment instead of basefragmentevent()
//    transaction.addToBackStack(null)
//    transaction.commit()
//}

