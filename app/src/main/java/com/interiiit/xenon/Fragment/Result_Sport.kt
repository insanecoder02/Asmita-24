package com.interiiit.xenon.Fragment

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
import com.interiiit.xenon.Adapter.ResultAdapter.Result_Sport_Adapter
import com.interiiit.xenon.DataClass.Score.MatchDetails
import com.interiiit.xenon.DataClass.Score.Matches
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentResultSportBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.interiiit.xenon.Activity.Main
import org.json.JSONException

class Result_Sport : Fragment() {
    private lateinit var binding: FragmentResultSportBinding
    private var reSport:MutableList<Matches> = mutableListOf()
    private lateinit var resultAdapter: Result_Sport_Adapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultSportBinding.inflate(layoutInflater, container, false)
        requireActivity().window.statusBarColor = 0xFF000000.toInt()
        binding.seeRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        resultAdapter = Result_Sport_Adapter(reSport,this)
        binding.seeRv.adapter = resultAdapter
        binding.seeRv.layoutManager = GridLayoutManager(requireContext(),2)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.refresh.setOnRefreshListener {
            fetchMatches()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        fetchMatches()
        binding.back.setOnClickListener {
            openDrawer()
        }
    }

    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

        private fun fetchMatches() {
        reSport.clear()
        val url = "https://app-admin-api.asmitaiiita.org/api/results/getResults"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val dataArray = response.getJSONArray("data")
                    val fixMap = mutableMapOf<String, MutableList<MatchDetails>>()

                    for (i in 0 until dataArray.length()) {
                        val jsonObject = dataArray.getJSONObject(i)
                        val matchName = jsonObject.getString("MatchName") ?: ""
                        val date = jsonObject.getString("Date") ?: ""
                        val time = jsonObject.getString("GroupStage") ?: ""
                        val type = jsonObject.getString("Type") ?: ""
                        val sport = jsonObject.getString("SportName")?:""
                        var clgName1: String =""
                        var clgImg1: String = ""
                        var clgName2: String = ""
                        var clgImg2: String = ""
                        var score1: String = "0"
                        var score2: String = "0"
                        var ov1: String = "0"
                        var ov2: String = "0"
                        var pt: String = "0"
                        var p1: String = "0"
                        var p2: String = "0"
                        var p3: String = "0"

                        when (type) {
                            "cricket" -> {
                                clgName1 = jsonObject.getString("ClgName1") ?: ""
                                clgImg1 = jsonObject.getString("ClgImg1") ?: ""
                                clgName2 = jsonObject.getString("ClgName2") ?: ""
                                clgImg2 = jsonObject.getString("ClgImg2") ?: ""
                                score1 = jsonObject.getString("Score1") ?: "0"
                                score2 = jsonObject.getString("Score2") ?: "0"
                                ov1 = jsonObject.getString("Over1") ?: "0"
                                ov2 = jsonObject.getString("Over2") ?: "0"
                            }
                            "football" -> {
                                clgName1 = jsonObject.getString("ClgName1") ?: ""
                                clgImg1 = jsonObject.getString("ClgImg1") ?: ""
                                clgName2 = jsonObject.getString("ClgName2") ?: ""
                                clgImg2 = jsonObject.getString("ClgImg2") ?: ""
                                pt = jsonObject.getString("Score") ?: "0"
                            }
                            else -> {
                                p1 = jsonObject.getString("Player1") ?: "0"
                                p2 = jsonObject.getString("Player2") ?: "0"
                                p3 = jsonObject.getString("Player3") ?: "0"
                            }
                        }
                        val sprtWise = MatchDetails(
                            matchName,
                            date,
                            time,
                            clgName1,
                            clgImg1,
                            clgName2,
                            clgImg2,
                            score1,
                            score2,
                            ov1,
                            ov2,
                            type,
                            sport,
                            pt,
                            p1,
                            p2,
                            p3
                        )

                        if (fixMap.containsKey(matchName)) {
                            fixMap[sport]?.add(sprtWise)
                        } else {
                            fixMap[sport] = mutableListOf(sprtWise)
                        }
                    }

                    for ((type, day) in fixMap) {
                        val teamSection = Matches(type, day)
                        reSport.add(teamSection)
                    }

                    resultAdapter.notifyDataSetChanged()
                    binding.resLot.visibility = View.INVISIBLE
                    binding.refresh.isRefreshing = false
                    binding.seeRv.visibility = View.VISIBLE
                    binding.normal.visibility = View.VISIBLE
                    binding.error.visibility = View.INVISIBLE
                } catch (e: JSONException) {
                    handleNetworkError()
//                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.e("fetchMatches", "Error parsing JSON", e)
                }
            },
            { error ->
                handleNetworkError()
//                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e("fetchMatches", "Error fetching data", error)
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun handleNetworkError() {
        binding.normal.visibility = View.INVISIBLE
        binding.error.visibility = View.VISIBLE
        binding.refresh.isRefreshing = false
        binding.loadBtn.setOnClickListener {
            fetchMatches()
            binding.refresh.isRefreshing = true
        }
    }
    fun onItemClick(item: Matches) {
        val bundle = Bundle()
        bundle.putString("name", item.matchName ?: "Name")
        bundle.putString("dayListJson", Gson().toJson(item.match))
        val nextFragment = results()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}