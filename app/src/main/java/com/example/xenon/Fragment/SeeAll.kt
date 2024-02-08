package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.xenon.Adapter.FixAdapter
import com.example.xenon.DataClass.AboutUs
import com.example.xenon.DataClass.Events
import com.example.xenon.DataClass.FixtureDataClass
import com.example.xenon.R
import com.example.xenon.databinding.FragmentSeeAllBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class SeeAll : Fragment() {
    private lateinit var binding:FragmentSeeAllBinding
    private lateinit var fixAdapter: FixAdapter
    private var fixture:MutableList<FixtureDataClass> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeeAllBinding.inflate(layoutInflater, container, false)
        binding.seeRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fixAdapter = FixAdapter(fixture,this)
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
            for (document in documents) {
                val name = document.getString("day") ?: ""
                val image = document.getString("image") ?: ""
                val item = FixtureDataClass(name)
                fixture.add(item)
            }
            fixAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.seeRv.visibility = View.VISIBLE
            binding.refresh.isRefreshing=false

        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun onItemClick(item: FixtureDataClass) {
        val bundle = Bundle()
        bundle.putString("name", item.day ?: "Name")
//        bundle.putString("date", item.date ?: "Date")
//        bundle.putString("image", item.image ?: "image")
//        bundle.putString("discription", item.discription ?: "Discription")
//        bundle.putString("heading", item.heading ?: "Heading")
//        bundle.putString("length", item.length ?: "Length")
//        bundle.putString("location", item.location ?: "Location")
//        bundle.putString("type", item.type ?: "Type")
        val nextFragment = Fixtures()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}