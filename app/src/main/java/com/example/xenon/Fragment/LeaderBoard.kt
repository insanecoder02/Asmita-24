package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.LeaderAdapter
import com.example.xenon.DataClass.ParticipateIIITS
import com.example.xenon.databinding.FragmentLeaderBoardBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LeaderBoard : Fragment() {
    private lateinit var binding: FragmentLeaderBoardBinding
    private var user: MutableList<ParticipateIIITS> = mutableListOf()
    private lateinit var useAdapter: LeaderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderBoardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useAdapter = LeaderAdapter(user)

        binding.leaderRv.layoutManager = LinearLayoutManager(context)
        binding.leaderRv.adapter = useAdapter

        fetchFromFirestore()
    }

    private fun fetchFromFirestore() {
        user.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("IIITS").orderBy("Points",Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("Name") ?: ""
                val Logo = document.getString("logo") ?: ""
                val Points = document.getLong("Points") ?: 0
                user.add(ParticipateIIITS(name, Logo, Points))
            }
            useAdapter.notifyDataSetChanged()
        }
    }
}
