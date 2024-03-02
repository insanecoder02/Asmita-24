package com.interiiit.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.interiiit.xenon.DataClass.ParticipateIIITS
import com.interiiit.xenon.databinding.FragmentParticipateBinding
import com.google.firebase.firestore.FirebaseFirestore
import ParticipatingAdapter
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.interiiit.xenon.Activity.Main
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interiiit.xenon.R

class participating_iiits : Fragment() {
    private lateinit var binding:FragmentParticipateBinding
    private var iiits: MutableList<ParticipateIIITS> = mutableListOf()
    private lateinit var partAdapter: ParticipatingAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParticipateBinding.inflate(layoutInflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("Participate", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logo = mapOf(
            "Allahabad" to R.drawable.allahabad,
            "Gwalior" to R.drawable.gwalior,
            "Kota" to R.drawable.kota,
            "Lucknow" to R.drawable.lucknow,
            "Manipur" to R.drawable.manipur,
            "Nagpur" to R.drawable.nagpur,
            "Pune" to R.drawable.pune,
            "Raichur" to R.drawable.raichur,
            "Ranchi" to R.drawable.ranchi,
            "Sonepat" to R.drawable.sonepat,
            "Surat" to R.drawable.surat,
            "Tiruchirappalli" to R.drawable.trichy,
            "Una" to R.drawable.una,
            "Vadodara" to R.drawable.vadodra,
            "Agartala" to R.drawable.agar,
            "Bhagalpur" to R.drawable.bhagalpur,
            "Bhopal" to R.drawable.bhopal,
            "Chittoor" to R.drawable.chittor,
            "Dharwad" to R.drawable.dharwad,
            "Guwahati" to R.drawable.guwahati,
            "Jabalpur" to R.drawable.jabalpur,
            "Kalyani" to R.drawable.kalyani,
            "Kancheepuram" to R.drawable.kancheepuram,
            "Kottayam" to R.drawable.kottayam,
            "Kurnool" to R.drawable.kurnool
        )
        partAdapter = ParticipatingAdapter(iiits,logo)
        binding.playingIits.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.playingIits.adapter = partAdapter
        binding.menu.setOnClickListener {
            openDrawer()
        }
        binding.refresh.setOnRefreshListener {
            fetch()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        fetchIfNeeded()
    }
    private fun fetchIfNeeded() {
        if (shouldFetchData()) {
            binding.playingIits.visibility = View.INVISIBLE
//            binding.resLot.visibility = View.VISIBLE
            fetch()
        } else {
            loadFromCache()
        }
    }
    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastPartFetchTime", 0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastFetchTime
        val fetchInterval = 24 * 60 * 60 * 1000 // 24 hours in milliseconds

        // Check if data has never been fetched or if more than 24 hours have passed since last fetch
        return !sharedPreferences.getBoolean("partDataFetched", false) || elapsedTime >= fetchInterval
    }
    private fun loadFromCache() {
        val json = sharedPreferences.getString("cachedPartData", null)
        if (json != null) {
            val type = object : TypeToken<List<ParticipateIIITS>>() {}.type
            val partList: List<ParticipateIIITS> = Gson().fromJson(json, type)
            iiits.clear()
            iiits.addAll(partList)
            partAdapter.notifyDataSetChanged()
//            binding.resLot.visibility = View.INVISIBLE
            binding.playingIits.visibility = View.VISIBLE
        }
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }
    private fun fetch() {
        iiits.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("IIITS").orderBy("Name").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("Name") ?: ""
                val Logo = document.getString("logo") ?: ""
                val point = document.getLong("point") ?: 0
                iiits.add(ParticipateIIITS(name,Logo,point))
            }
            Log.d("hello","data fetched")
            partAdapter.notifyDataSetChanged()
            binding.refresh.isRefreshing = false
//            binding.resLot.visibility = View.INVISIBLE
            binding.playingIits.visibility = View.VISIBLE
            binding.normal.visibility = View.VISIBLE
            binding.error.visibility = View.INVISIBLE
            updateSharedPreferences()
        }.addOnFailureListener{ e->
            handleNetworkError()
//            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
    private fun handleNetworkError() {
        binding.normal.visibility = View.INVISIBLE
        binding.error.visibility = View.VISIBLE
        binding.refresh.isRefreshing = false
        binding.loadBtn.setOnClickListener {
            fetch()
        }
    }
    private fun updateSharedPreferences() {
        val json = Gson().toJson(iiits)
        sharedPreferences.edit().apply {
            putBoolean("partDataFetched", true)
            putLong("lastPartFetchTime", System.currentTimeMillis())
            putString("cachedPartData", json)
            apply()
        }
    }
}