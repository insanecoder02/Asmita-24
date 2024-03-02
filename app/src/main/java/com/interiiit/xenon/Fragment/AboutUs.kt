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
import com.interiiit.xenon.Adapter.AboutAdapter
import com.interiiit.xenon.DataClass.AboutUs
import com.interiiit.xenon.databinding.FragmentAboutUsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AboutUs : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding
    private val abtus: MutableList<AboutUs> = mutableListOf()
    private lateinit var abtAdapter: AboutAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("About",Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        abtAdapter = AboutAdapter(abtus)
        binding.aboutRV.adapter = abtAdapter
        binding.aboutRV.layoutManager = LinearLayoutManager(requireContext())

        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.refresh.setOnRefreshListener {
            fetchFromFirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        fetchIfNeeded()
    }
    private fun fetchIfNeeded() {
        if(shouldFetchData()){
            fetchFromFirestore()
            binding.aboutRV.visibility = View.INVISIBLE
            binding.resLot.visibility = View.VISIBLE
        }
        else{
            loadFromCache()
        }
    }
    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastAboutFetchTime",0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime-lastFetchTime
        val fetchInterval = 48*60*60*1000
        return !sharedPreferences.getBoolean("dataAboutFetched",false) || elapsedTime>=fetchInterval
    }
    private fun loadFromCache() {
        val json = sharedPreferences.getString("cachedAboutData",null)
        if(json!=null){
            val type = object :TypeToken<List<AboutUs>>() {}.type
            val aboutList:List<AboutUs> = Gson().fromJson(json,type)
            abtus.clear()
            abtus.addAll(aboutList)
            abtAdapter.notifyDataSetChanged()
            binding.aboutRV.visibility = View.VISIBLE
            binding.resLot.visibility = View.INVISIBLE
        }
    }
    private fun fetchFromFirestore() {
        abtus.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("AboutUs").orderBy("no").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("info") ?: ""
                val image = document.getString("image") ?: ""
                val item = AboutUs(name, image)
                abtus.add(item)
            }
            abtAdapter.notifyDataSetChanged()
            binding.refresh.isRefreshing = false
            binding.resLot.visibility = View.INVISIBLE
            binding.aboutRV.visibility = View.VISIBLE
            binding.normal.visibility = View.VISIBLE
            binding.error.visibility = View.INVISIBLE
            updateSharedPreferences()
        }.addOnFailureListener { e ->
            handleNetworkError()
//            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
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
        val json = Gson().toJson(abtus)
        sharedPreferences.edit().apply{
            putBoolean("dataAboutFetched",true)
            putLong("lastAboutFetchTime",System.currentTimeMillis())
            putString("cachedAboutData",json)
            apply()
        }
    }
}