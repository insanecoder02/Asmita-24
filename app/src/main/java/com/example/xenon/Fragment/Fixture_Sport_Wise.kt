package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.xenon.Adapter.FixAdapter
import com.example.xenon.Adapter.Fixture_Sport_Adapter
import com.example.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.example.xenon.DataClass.FixtureDataClass.FixtureSportDataClass
import com.example.xenon.R
import com.example.xenon.databinding.FragmentFixtureSportWiseBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class Fixture_Sport_Wise : Fragment() {
    private lateinit var binding:FragmentFixtureSportWiseBinding
    private lateinit var fixAdapter: Fixture_Sport_Adapter
    private var fixture:MutableList<FixtureSportDataClass> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixtureSportWiseBinding.inflate(layoutInflater, container, false)
        binding.seeRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fixAdapter = Fixture_Sport_Adapter(fixture,this)
        binding.seeRv.adapter = fixAdapter
        binding.seeRv.layoutManager = GridLayoutManager(requireContext(),2)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.refresh.setOnRefreshListener {
            fetch()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        fetch()
    }
    private fun fetch(){
        fixture.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Fixture").orderBy("no").get().addOnSuccessListener { documents ->
            val fixMap = mutableMapOf<String, MutableList<Fixture_Day_DataClass>>()
            for (document in documents) {
                val name = document.getString("day") ?: ""
                val image = document.getString("image") ?: ""
                val type = document.getString("type") ?: ""
                val dayWise = Fixture_Day_DataClass(name)
                if (fixMap.containsKey(type)) {
                    fixMap[type]?.add(dayWise)
                } else {
                    fixMap[type] = mutableListOf(dayWise)
                }
            }
            for ((type, day) in fixMap) {
                val teamSection = FixtureSportDataClass(type, day)
                fixture.add(teamSection)
            }
            fixAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.seeRv.visibility = View.VISIBLE
            binding.refresh.isRefreshing=false

        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun onItemClick(item: FixtureSportDataClass) {
        val bundle = Bundle()
        bundle.putString("name", item.type ?: "Name")
        bundle.putString("dayListJson", Gson().toJson(item.fix))
//        bundle.putString("date", item.date ?: "Date")
//        bundle.putString("image", item.image ?: "image")
//        bundle.putString("discription", item.discription ?: "Discription")
//        bundle.putString("heading", item.heading ?: "Heading")
//        bundle.putString("length", item.length ?: "Length")
//        bundle.putString("location", item.location ?: "Location")
//        bundle.putString("type", item.type ?: "Type")
        val nextFragment = Fixture_Day_Wise()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}