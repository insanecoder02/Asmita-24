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
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.interiiit.xenon.Adapter.Fixture_Sport_Adapter
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.DataClass.FixtureDataClass.FixtureSportDataClass
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentFixtureSportWiseBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interiiit.xenon.DataClass.AboutUs
import org.json.JSONException
import org.json.JSONObject

class Fixture_Sport_Wise : Fragment() {
    private lateinit var binding:FragmentFixtureSportWiseBinding
    private lateinit var fixAdapter: Fixture_Sport_Adapter
    private lateinit var sharedPreferences: SharedPreferences
    private var fixture:MutableList<FixtureSportDataClass> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixtureSportWiseBinding.inflate(layoutInflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("Fix", Context.MODE_PRIVATE)
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
//        binding.loadBtn.setOnClickListener{
//            fetch()
//        }
        fetchIfNeeded()
    }
    private fun fetchIfNeeded() {
        if(shouldFetchData()){
            fetch()
            binding.seeRv.visibility = View.INVISIBLE
            binding.resLot.visibility = View.VISIBLE
        }
        else{
            loadFromCache()
        }
    }
    private fun shouldFetchData(): Boolean {
        val lastFetchTime = sharedPreferences.getLong("lastFixFetchTime",0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime-lastFetchTime
        val fetchInterval = 2*60*1000
        return !sharedPreferences.getBoolean("dataFixFetched",false) || elapsedTime>=fetchInterval
    }
    private fun loadFromCache() {
        val json = sharedPreferences.getString("cachedFixData",null)
        if(json!=null){
            val type = object : TypeToken<List<FixtureSportDataClass>>() {}.type
            val fixList:List<FixtureSportDataClass> = Gson().fromJson(json,type)
            fixture.clear()
            fixture.addAll(fixList)
            fixAdapter.notifyDataSetChanged()
            binding.seeRv.visibility = View.VISIBLE
            binding.resLot.visibility = View.INVISIBLE
        }
    }
    private fun fetch() {
        fixture.clear()
        val url = "https://app-admin-api.asmitaiiita.org/api/fixtures"
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val dataArray = response.getJSONArray("data")
                    val sortedDataArray = mutableListOf<JSONObject>()
                    for (i in 0 until dataArray.length()) {
                        sortedDataArray.add(dataArray.getJSONObject(i))
                    }
                    sortedDataArray.sortBy { it.getString("Day") }
                    val fixMap = mutableMapOf<String, MutableList<Fixture_Day_DataClass>>()
                    for (i in 0 until sortedDataArray.size) {
                        val jsonObject = sortedDataArray[i]
                        val name = jsonObject.getString("Day") ?: ""
                        val type = jsonObject.getString("Sport") ?: ""
                        val html = jsonObject.getString("HTMLString") ?: ""
                        val dayWise = Fixture_Day_DataClass(name, html)
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
                    binding.normal.visibility = View.VISIBLE
                    binding.error.visibility = View.INVISIBLE
                    binding.refresh.isRefreshing = false
                    updateSharedPreferences()
                } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                    handleNetworkError()
                    Log.e("fetch", "Error parsing JSON", e)
                }
            },
            { error ->
//                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                handleNetworkError()
                Log.e("fetch", "Error fetching data", error)
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
    private fun updateSharedPreferences() {
        val json = Gson().toJson(fixture)
        sharedPreferences.edit().apply{
            putBoolean("dataFixFetched",true)
            putLong("lastFixFetchTime",System.currentTimeMillis())
            putString("cachedFixData",json)
            apply()
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
    fun onItemClick(item: FixtureSportDataClass) {
        val bundle = Bundle()
        bundle.putString("name", item.type ?: "Name")
        bundle.putString("dayListJson", Gson().toJson(item.fix))
        val nextFragment = Fixture_Day_Wise()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}