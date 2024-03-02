package com.interiiit.xenon.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.android.volley.Request
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.interiiit.xenon.Activity.Main
import com.interiiit.xenon.Adapter.FixAdapter
import com.interiiit.xenon.Adapter.ResultAdapter.ResultAdapter
import com.interiiit.xenon.DataClass.FixtureDataClass.FixtureSportDataClass
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.DataClass.Score.MatchDetails
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentHomeBinding
import com.interiiit.xenon.other.AutoScroll
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import org.json.JSONException

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fixAdapter: FixAdapter
    private lateinit var resultAdapter: ResultAdapter
    private var fixture: MutableList<FixtureSportDataClass> = mutableListOf()
    private var upcomingMatchesList: MutableList<MatchDetails> = mutableListOf()
    private lateinit var firestore: FirebaseFirestore
    private val autoScrollManagers = mutableListOf<AutoScroll>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        requireActivity().window.statusBarColor = 0xFFE9BD3E.toInt()
        binding.upcommingMatchsRV.visibility = View.INVISIBLE
        binding.resultMRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        binding.matLot.visibility = View.VISIBLE
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()

        resultAdapter = ResultAdapter(upcomingMatchesList)
        binding.resultMRv.adapter = resultAdapter
        binding.resultMRv.layoutManager = CarouselLayoutManager(
            false, false, 0.7F, false, false, true, LinearLayoutManager.HORIZONTAL
        )
        fixAdapter = FixAdapter(fixture,this)
        binding.upcommingMatchsRV.adapter = fixAdapter
        binding.upcommingMatchsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.refresh.setOnRefreshListener {
            fetchResult()
            fetchFixtures()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        fetchResult()
        fetchFixtures()
        binding.seeText.setOnClickListener {
            loadFragment(Fixture_Sport_Wise())
        }
        binding.events.setOnClickListener {
            loadFragment(Event())
        }
        binding.leaderboard.setOnClickListener {
            loadFragment(LeaderBoard())
        }
        binding.results.setOnClickListener {
            loadFragment(Result_Sport())
        }
        binding.menu.setOnClickListener {
            openDrawer()
        }
        binding.fixture.setOnClickListener {
            loadFragment(Fixture_Sport_Wise())
        }
        rotor(binding.upcommingMatchsRV)
        val autoScrollManager = AutoScroll(binding.resultMRv)
        autoScrollManager.startAutoScroll()
        autoScrollManagers.add(autoScrollManager)
    }

    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

    private fun rotor(recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val autoScroll = AutoScroll(recyclerView)
        autoScroll.startAutoScroll()
        autoScrollManagers.add(autoScroll)
    }

    private fun loadFragment(fragment: Fragment) {
        requireActivity().window.statusBarColor = 0xFF000000.toInt()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun fetchFixtures() {
        fixture.clear()
        val url = "https://app-admin-api.asmitaiiita.org/api/fixtures"
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val fixMap = mutableMapOf<String, MutableList<Fixture_Day_DataClass>>()
                    val dataArray = response.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val jsonObject = dataArray.getJSONObject(i)
                        val name = jsonObject.getString("Day") ?: ""
                        val type = jsonObject.getString("Sport") ?: ""
                        val html = jsonObject.getString("HTMLString") ?: ""
                        val dayWise = Fixture_Day_DataClass(name,html)
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
                    binding.upcommingMatchsRV.visibility = View.VISIBLE
                    binding.refresh.isRefreshing = false
                } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT/2).show()
                    Log.e("fetchFixtures", "Error parsing JSON", e)
                }
            },
            { error ->
//                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT/2).show()
                Log.e("fetchFixtures", "Error fetching fixtures", error)
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
    private fun fetchResult() {
        val allResult = mutableListOf<MatchDetails>()
        val url = "https://app-admin-api.asmitaiiita.org/api/results/getResults"
        val requestQueue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val dataArray = response.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val jsonObject = dataArray.getJSONObject(i)
                        val matchName = jsonObject.getString("MatchName") ?: ""
                        val date = jsonObject.getString("Date") ?: ""
                        val time = jsonObject.getString("GroupStage") ?: ""
                        val type = jsonObject.getString("Type") ?: ""
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
                        allResult.add(
                            MatchDetails(
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
                                pt,
                                p1,
                                p2,
                                p3
                            )
                        )
                    }
                    upcomingMatchesList.clear()
                    upcomingMatchesList.addAll(allResult.take(5))
                    resultAdapter.notifyDataSetChanged()
                    binding.matLot.visibility = View.INVISIBLE
                    binding.resLot.visibility = View.INVISIBLE
                    binding.upcommingMatchsRV.visibility = View.VISIBLE
                    binding.resultMRv.visibility = View.VISIBLE
                    binding.refresh.isRefreshing = false
                } catch (e: JSONException) {
//                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT/2).show()
                    Log.e("fetchResult", "Error parsing JSON", e)
                }
            },
            { error ->
//                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT/2).show()
                Log.e("fetchResult", "Error fetching data", error)
            }
        )
        requestQueue.add(jsonObjectRequest)
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

//    fun onResClick(item: Matches) {
//        val bundle = Bundle()
//        bundle.putString("name", item.type ?: "Name")
//        bundle.putString("dayListJson", Gson().toJson(item.match))
//        val nextFragment = Fixture_Day_Wise()
//        nextFragment.arguments = bundle
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container, nextFragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
}