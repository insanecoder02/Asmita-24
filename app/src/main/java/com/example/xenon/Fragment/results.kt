package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.ResultAdapter
import com.example.xenon.Adapter.Score.UpcomingMatchAdapter
import com.example.xenon.DataClass.Score.MatchDetails
import com.example.xenon.databinding.FragmentResultsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class results : Fragment() {
    private lateinit var binding:FragmentResultsBinding
    private lateinit var resultAdapter: ResultAdapter
    private var upcomingMatchesList: MutableList<MatchDetails> = mutableListOf()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultsBinding.inflate(layoutInflater, container, false)
        binding.resultRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        resultAdapter = ResultAdapter(upcomingMatchesList)
        binding.resultRv.adapter = resultAdapter
        binding.resultRv.layoutManager = LinearLayoutManager(requireContext())
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.refresh.setOnRefreshListener {
            fetchMatches()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
        fetchMatches()
    }

    private fun fetchMatches() {
        upcomingMatchesList.clear()
        firestore.collection("Schedule").orderBy("type").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val matchName = document.getString("matchname") ?: ""
                val date = document.getString("date") ?: ""
                val time = document.getString("time")?:""
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
                upcomingMatchesList.add(MatchDetails(matchName,date,time,clgName1,clgImg1,clgName2,clgImg2,score1,score2,ov1,ov2,type,pt,p1,p2,p3))
            }
            resultAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.refresh.isRefreshing=false
            binding.resultRv.visibility = View.VISIBLE

        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}