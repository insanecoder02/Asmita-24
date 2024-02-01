package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.Team.WingAdapter
import com.example.xenon.DataClass.Team.TeamMember
import com.example.xenon.DataClass.Team.TeamSection
import com.example.xenon.Adapter.EventsAdapter.FeaturedEventsAdapter
import com.example.xenon.Adapter.Team.EventsAdapter
import com.example.xenon.DataClass.EveDataClass
import com.example.xenon.DataClass.Events
import com.example.xenon.R
import com.example.xenon.databinding.FragmentEventBinding
import com.google.firebase.firestore.FirebaseFirestore

class Event : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private var eventClass: MutableList<EveDataClass> = mutableListOf()
    private lateinit var wingAdapter: EventsAdapter
    private var featuredClass : MutableList<Events> = mutableListOf()
    private lateinit var eventsAdapter: FeaturedEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wingAdapter = EventsAdapter(requireContext(), eventClass)
        binding.teamRV.adapter = wingAdapter
        binding.teamRV.layoutManager = LinearLayoutManager(requireContext())

        eventsAdapter = FeaturedEventsAdapter(requireContext(),featuredClass,this)
        binding.featuredRv.adapter = eventsAdapter
        binding.featuredRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

//        fetchFromFirestore1()
        fetchFromFirestore2()
    }

//    private fun fetchFromFirestore1() {
//        eventClass.clear()
//        val db = FirebaseFirestore.getInstance()
//        db.collection("Event").get().addOnSuccessListener { documents ->
//            val wingMap = mutableMapOf<String, MutableList<TeamMember>>()
//            for (document in documents) {
//                val name = document.getString("name") ?: ""
//                val img = document.getString("image") ?: ""
//                val role = document.getString("role") ?: ""
//                val wing = document.getString("wing") ?: ""
//                val teamMember = TeamMember(name, img)
//                if (wingMap.containsKey(wing)) {
//                    wingMap[wing]?.add(teamMember)
//                } else {
//                    wingMap[wing] = mutableListOf(teamMember)
//                }
//            }
//            for ((wing, members) in wingMap) {
//                val teamSection = TeamSection(wing, members)
//                eventClass.add(teamSection)
//            }
//            wingAdapter.notifyDataSetChanged()
//        }.addOnFailureListener { exception ->
//            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun fetchFromFirestore2() {
        featuredClass.clear()
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

                    featuredClass.add(Events(name,date,image,discription,heading,length,location,type))
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

        val nextFragment = sport_detail()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}












































//val bundle = Bundle()
//bundle.putString("name",item.name ?: "Name")
//bundle.putString("date",item.date ?: "Date")
//bundle.putString("image",item.image ?: "image")
//bundle.putString("discription",item.discription ?: "Discription")
//bundle.putString("heading",item.heading ?: "Heading")
//bundle.putString("length",item.length ?: "Length")
//bundle.putString("location",item.location ?: "Location")
//bundle.putString("type",item.type ?: "Type")
//val nextFragment = Gallery()
//nextFragment.arguments = bundle
//val transaction = requireActivity().supportFragmentManager.beginTransaction()
//transaction.replace(
//R.id.fragment_container, nextFragment
//)
//transaction.addToBackStack(null)
//transaction.commit()





