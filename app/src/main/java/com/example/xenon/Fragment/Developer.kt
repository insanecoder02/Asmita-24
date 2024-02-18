package com.example.xenon.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.DevAdapter
import com.example.xenon.DataClass.DeveloperDataClass
import com.example.xenon.databinding.FragmentDeveloperBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Developer : Fragment() {
    private lateinit var binding: FragmentDeveloperBinding
    private val dev: MutableList<DeveloperDataClass> = mutableListOf()
    private lateinit var devAdapter: DevAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeveloperBinding.inflate(layoutInflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("Developer", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devAdapter = DevAdapter(dev)
        binding.devRv.adapter = devAdapter
        binding.devRv.layoutManager = LinearLayoutManager(requireContext())
        binding.refresh.setOnRefreshListener {
            fetch()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        binding.menu.setOnClickListener {
            openDrawer()
        }
        fetchIfNeeded()
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

    private fun fetchIfNeeded() {
        if (shouldFetchData()) {
            fetch()
        } else {
            loadFromCache()
        }
    }

    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastDevFetchTime", 0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastFetchTime
        val fetchInterval = 24 * 60 * 60 * 1000 // 24 hours in milliseconds

        // Check if data has never been fetched or if more than 24 hours have passed since last fetch
        return !sharedPreferences.getBoolean("dataDevFetched", false) || elapsedTime >= fetchInterval
    }

    private fun loadFromCache() {
        val json = sharedPreferences.getString("cachedDeveloperData", null)
        if (json != null) {
            val type = object : TypeToken<List<DeveloperDataClass>>() {}.type
            val developerList: List<DeveloperDataClass> = Gson().fromJson(json, type)
            dev.clear()
            dev.addAll(developerList)
            devAdapter.notifyDataSetChanged()
        }
    }

    private fun fetch() {
        dev.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Developer").orderBy("no").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val pos = document.getString("pos") ?: ""
                val image = document.getString("image") ?: ""
                val item = DeveloperDataClass(name, pos, image)
                dev.add(item)
            }
            devAdapter.notifyDataSetChanged()
            Log.d("hello","data fetched")
            binding.refresh.isRefreshing = false
            updateSharedPreferences()
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Failed to fetch data: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            binding.refresh.isRefreshing = false
        }
    }

    private fun updateSharedPreferences() {
        val json = Gson().toJson(dev)
        sharedPreferences.edit().apply {
            putBoolean("dataDevFetched", true)
            putLong("lastDevFetchTime", System.currentTimeMillis())
            putString("cachedDeveloperData", json)
            apply()
        }
    }
}
