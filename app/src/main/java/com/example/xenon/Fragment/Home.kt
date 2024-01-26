package com.example.xenon.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.Score.LiveMatchAdapter
import com.example.xenon.Adapter.Score.UpcommingMatchAdapter
import com.example.xenon.DataClass.Score.MatchDetails
import com.example.xenon.R
import com.example.xenon.databinding.FragmentHomeBinding
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview


class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var upcommingmatchesadapter: UpcommingMatchAdapter
    private  lateinit var livematchadapter : LiveMatchAdapter
    private var upcommingMatchesList: MutableList<MatchDetails> = mutableListOf()
    private  var liveMatchesList: MutableList<MatchDetails> = mutableListOf()
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        firestore = FirebaseFirestore.getInstance()


        livematchadapter = LiveMatchAdapter(liveMatchesList)

        binding.liveMatchRV.adapter = livematchadapter
        (binding.liveMatchRV as CarouselRecyclerview).setInfinite(true)
        binding.liveMatchRV.layoutManager =
            com.jackandphantom.carouselrecyclerview.CarouselLayoutManager(
                true, true, 0.7F, true, true, true, LinearLayoutManager.HORIZONTAL
            )






        upcommingmatchesadapter = UpcommingMatchAdapter(upcommingMatchesList)
        binding.upcommingMatchsRV.adapter = upcommingmatchesadapter
        binding.upcommingMatchsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)



        fetchMatches()

        binding.events.setOnClickListener {
            val fragment = Event()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.leaderboard.setOnClickListener {
            val fragment = Leaderboard_Fragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.results.setOnClickListener{
            val fragment = results()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    private fun fetchMatches() {
        firestore.collection("Schedule").get().addOnSuccessListener { documents ->
            for (document in documents) {

                val matchName = document.getString("matchName") ?: ""
                val date = document.getString("date") ?: ""
                val time = document.getString("time") ?: ""
                val clgName1 = document.getString("clgName1") ?: ""
                val clgImg1 = document.getString("clgImg1") ?: ""
                val clgName2 = document.getString("clgName2") ?: ""
                val clgImg2 = document.getString("clgImg2") ?: ""
                val live = document.getString("live") ?: ""
                val score1 = document.getString("score1") ?: "0"
                val score2 = document.getString("score2") ?: "0"


                val match = MatchDetails(
                    matchName = matchName,
                    date = date,
                    time = time,
                    clgName1 = clgName1,
                    clgImg1 = clgImg1,
                    clgName2 = clgName2,
                    clgImg2 = clgImg2,
                    live = live,
                    score1 = score1,
                    score2 = score2,
                )

                if (live == "y") {
                    liveMatchesList.add(match)
                } else {
                    upcommingMatchesList.add(match)
                }
            }

            // Notify the adapters after fetching matches
            livematchadapter.notifyDataSetChanged()
            upcommingmatchesadapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }
    }
}