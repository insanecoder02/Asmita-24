package com.example.xenon.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.SponserAdapter
import com.example.xenon.DataClass.DeveloperDataClass
import com.example.xenon.DataClass.Sponser
import com.example.xenon.R
import com.example.xenon.databinding.FragmentSponsorsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Sponsors : Fragment() {
    private lateinit var binding:FragmentSponsorsBinding
    private val spon:MutableList<Sponser> = mutableListOf()
    private lateinit var sponsAdapter: SponserAdapter
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSponsorsBinding.inflate(layoutInflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("Sponser", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sponsAdapter = SponserAdapter(spon)
        binding.sponsRV.adapter = sponsAdapter
        binding.sponsRV.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.menu.setOnClickListener {
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
            fetchFromFirestore()
        } else {
            loadFromCache()
        }
    }
    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastSponFetchTime", 0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastFetchTime
        val fetchInterval = 24 * 60 * 60 * 1000 // 24 hours in milliseconds

        // Check if data has never been fetched or if more than 24 hours have passed since last fetch
        return !sharedPreferences.getBoolean("dataSponFetched", false) || elapsedTime >= fetchInterval
    }
    private fun loadFromCache() {
        val json = sharedPreferences.getString("cachedSponData", null)
        if (json != null) {
            val type = object : TypeToken<List<Sponser>>() {}.type
            val sponsList: List<Sponser> = Gson().fromJson(json, type)
            spon.clear()
            spon.addAll(sponsList)
            sponsAdapter.notifyDataSetChanged()
        }
    }

    private fun fetchFromFirestore() {
        spon.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Sponser").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val image = document.getString("image")?:""
                val item = Sponser(name,image)
                spon.add(item)
            }
            sponsAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.sponsRV.visibility = View.VISIBLE
            binding.refresh.isRefreshing = false
            updateSharedPreferences()

        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateSharedPreferences() {
        val json = Gson().toJson(spon)
        sharedPreferences.edit().apply {
            putBoolean("dataSponFetched", true)
            putLong("lastSponFetchTime", System.currentTimeMillis())
            putString("cachedSponData", json)
            apply()
        }
    }
}