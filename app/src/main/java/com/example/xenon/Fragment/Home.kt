package com.example.xenon.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.Adapter.ResultAdapter
import com.example.xenon.Adapter.Score.UpcomingMatchAdapter
import com.example.xenon.DataClass.Score.MatchDetails
import com.example.xenon.R
import com.example.xenon.databinding.FragmentHomeBinding
import com.example.xenon.other.AutoScroll
import com.google.firebase.firestore.FirebaseFirestore
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var upcommingmatchesadapter: UpcomingMatchAdapter
    private lateinit var resultAdapter: ResultAdapter
    private var upcomingMatchesList: MutableList<MatchDetails> = mutableListOf()
    private lateinit var firestore: FirebaseFirestore
    private val autoScrollManagers = mutableListOf<AutoScroll>()
    private var isRecyclerViewHeld = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
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
            true, false, 0.7F, false, true, true, LinearLayoutManager.HORIZONTAL
        )

        upcommingmatchesadapter = UpcomingMatchAdapter(upcomingMatchesList)
        binding.upcommingMatchsRV.adapter = upcommingmatchesadapter
        binding.upcommingMatchsRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        fetchMatches()

        binding.events.setOnClickListener {
            loadFragment(Event())
        }

        binding.leaderboard.setOnClickListener {
            loadFragment(LeaderBoard())
        }

        binding.results.setOnClickListener {
            loadFragment(results())
        }

//        binding.resultMRv.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    isRecyclerViewHeld = true
//                    autoScrollManagers.forEach { it.stopAutoScroll() }
//                }
//
//                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                    isRecyclerViewHeld = false
//                    autoScrollManagers.forEach { it.startAutoScroll(5000) }
//                }
//            }
//            false
//        }
//        binding.upcommingMatchsRV.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    isRecyclerViewHeld = true
//                    autoScrollManagers.forEach { it.stopAutoScroll() }
//                }
//
//                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                    isRecyclerViewHeld = false
//                    autoScrollManagers.forEach { it.startAutoScroll(5000) }
//                }
//            }
//            false
//        }

//        rotor(binding.upcommingMatchsRV)
        val autoScrollManager = AutoScroll(binding.resultMRv)
        autoScrollManager.startAutoScroll(5000)
        autoScrollManagers.add(autoScrollManager)
    }

    private fun rotor(recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val autoScroll = AutoScroll(recyclerView)
        autoScroll.startAutoScroll(5000)
        autoScrollManagers.add(autoScroll)
    }

    override fun onPause() {
        super.onPause()
        autoScrollManagers.forEach { it.stopAutoScroll() }
    }

//    override fun onResume() {
//        super.onResume()
//        if (!isRecyclerViewHeld) {
//            autoScrollManagers.forEach { it.startAutoScroll(5000) }
//        }
//    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fetchMatches() {
        upcomingMatchesList.clear()
        firestore.collection("Schedule").limit(5).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val matchName = document.getString("matchname") ?: ""
                val date = document.getString("date") ?: ""
                val time = document.getString("time") ?: ""
                val clgName1 = document.getString("clgname1") ?: ""
                val clgImg1 = document.getString("clgimg1") ?: ""
                val clgName2 = document.getString("clgname2") ?: ""
                val clgImg2 = document.getString("clgimg2") ?: ""
                val score1 = document.getString("score1") ?: "0"
                val score2 = document.getString("score2") ?: "0"
                val ov1 = document.getString("over1") ?: "0"
                val ov2 = document.getString("over2") ?: "0"
                val type = document.getString("type") ?: "0"
                val pt = document.getString("point") ?: "0"
                val p1 = document.getString("player1") ?: "0"
                val p2 = document.getString("player2") ?: "0"
                val p3 = document.getString("player3") ?: "0"
                upcomingMatchesList.add(
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
            upcommingmatchesadapter.notifyDataSetChanged()
            resultAdapter.notifyDataSetChanged()
            binding.matLot.visibility = View.INVISIBLE
            binding.resLot.visibility = View.INVISIBLE
            binding.upcommingMatchsRV.visibility = View.VISIBLE
            binding.resultMRv.visibility = View.VISIBLE
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}