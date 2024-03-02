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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.interiiit.xenon.Activity.Main
import com.interiiit.xenon.Adapter.SponserAdapter
import com.interiiit.xenon.DataClass.Sponser
import com.interiiit.xenon.databinding.FragmentSponsorsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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
            binding.sponsRV.visibility = View.INVISIBLE
            binding.resLot.visibility = View.VISIBLE
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
            binding.resLot.visibility = View.INVISIBLE
            binding.sponsRV.visibility = View.VISIBLE
        }
    }

    private fun fetchFromFirestore() {
        spon.clear()
        val db = FirebaseFirestore.getInstance()
        try{
        db.collection("Sponser").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val image = document.getString("image") ?: ""
                val item = Sponser(name, image)
                spon.add(item)
            }
            sponsAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.sponsRV.visibility = View.VISIBLE
            binding.refresh.isRefreshing = false
            binding.normal.visibility = View.VISIBLE
            binding.error.visibility = View.INVISIBLE
            updateSharedPreferences()
        }
        }catch (e: FirebaseFirestoreException) {
            if (e.code == FirebaseFirestoreException.Code.UNAVAILABLE) {
                handleNetworkError()
            } else {
//                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT/2).show()
                Log.e("FirestoreFetch", "Error fetching data from Firestore", e)
            }
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
        val json = Gson().toJson(spon)
        sharedPreferences.edit().apply {
            putBoolean("dataSponFetched", true)
            putLong("lastSponFetchTime", System.currentTimeMillis())
            putString("cachedSponData", json)
            apply()
        }
    }
}