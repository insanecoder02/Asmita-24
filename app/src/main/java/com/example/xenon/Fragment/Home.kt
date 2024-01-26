package com.example.xenon.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Activity.Leaderboard
import com.example.xenon.Adapter.Score.MatchAdapter
import com.example.xenon.DataClass.Score.MatchDetails
import com.example.xenon.R
import com.example.xenon.databinding.FragmentHomeBinding
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview


class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var matAdapter: MatchAdapter
    private var mat: MutableList<MatchDetails> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.liveMatch.layoutManager =
            com.jackandphantom.carouselrecyclerview.CarouselLayoutManager(
                true, true, 0.5F, true, true, true, LinearLayoutManager.HORIZONTAL
            )
        (binding.liveMatch as CarouselRecyclerview).setInfinite(true)
        matAdapter = MatchAdapter(mat)
        binding.matchRV.adapter = matAdapter
        binding.matchRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.events.setOnClickListener {
            val fragment = Event()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.leaderboard.setOnClickListener {
            startActivity(Intent(requireContext(), Leaderboard::class.java))
        }
        //fetchSystemDateTime()
    }
   /* private fun fetchSystemDateTime() {
        try {
            val currentTimeMillis = System.currentTimeMillis()
            val targetDateString = bringmeDateboy
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val targetDate = sdf.parse(targetDateString)
            val timeDifferenceMillis = targetDate.time - currentTimeMillis

            startCountdown(timeDifferenceMillis)
        } catch (e: ParseException) {
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

        fetchFromFirestore()
    }
    private fun fetchFromFirestore() {
        mat.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Schedule").orderBy("no").get().addOnSuccessListener { documents ->
            val wingMap = mutableMapOf<String, MutableList<MatchDetails>>()
            for (document in documents) {
                val matname = document.getString("name") ?: ""
                val date = document.getString("date") ?: ""
                val time = document.getString("time") ?: ""
                val clgname1 = document.getString("clg1") ?: ""
                val clgname2 = document.getString("clg2") ?: ""
                val clgimg1 = document.getString("img1") ?: ""
                val clgimg2 = document.getString("img2") ?: ""
                val live = document.getString("live") ?: ""

                val match =
                    MatchDetails(matname, date, time, clgname1, clgimg1, clgname2, clgimg2, live)
                mat.add(match)
            }
            matAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        countDownTimer.start()
        isTimerRunning = true
    }*/
   /* private fun updateUI(countdownText: String) {
        val hourMinSec: List<String> = countdownText.split(":")
        binding.hours1.text = (hourMinSec[1].toInt() / 10).toString()
        binding.minutes1.text = (hourMinSec[2].toInt() / 10).toString()
        binding.days1.text = (hourMinSec[0].toInt() / 10).toString()
        binding.hours2.text = (hourMinSec[1].toInt() % 10).toString()
        binding.minutes2.text = (hourMinSec[2].toInt() % 10).toString()
        binding.days2.text = (hourMinSec[0].toInt() % 10).toString()
    }
    private fun resetUI() {
        binding.days1.text = "0"
        binding.hours1.text = "0"
        binding.minutes1.text = "0"
        binding.days2.text = "0"
        binding.hours2.text = "0"
        binding.minutes2.text = "0"
        isTimerRunning = false

    }*/
}