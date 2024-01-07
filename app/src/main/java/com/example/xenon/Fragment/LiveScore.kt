package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.Score.MatchAdapter
import com.example.xenon.DataClass.Score.MatchDetails
import com.example.xenon.DataClass.Score.Matches
import com.example.xenon.databinding.FragmentLiveScoreBinding
import com.google.firebase.firestore.FirebaseFirestore

class LiveScore : Fragment() {
    private lateinit var binding: FragmentLiveScoreBinding
    private lateinit var matAdapter: MatchAdapter
    private var mat: MutableList<Matches> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveScoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matAdapter = MatchAdapter(requireContext(), mat)
        binding.matchRV.adapter = matAdapter
        binding.matchRV.layoutManager = LinearLayoutManager(requireContext())

        fetchFromFirestore()

    }

    private fun fetchFromFirestore() {
        mat.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Schedule").orderBy("no").get().addOnSuccessListener { documents ->
            val wingMap = mutableMapOf<String, MutableList<MatchDetails>>()
            for (document in documents) {
                val mattype = document.getString("type") ?: ""
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
                if (wingMap.containsKey(mattype)) {
                    wingMap[mattype]?.add(match)
                } else {
                    wingMap[mattype] = mutableListOf(match)
                }
            }
            for ((mattype, members) in wingMap) {
                val teamSection = Matches(mattype, members)
                mat.add(teamSection)
            }
            matAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}