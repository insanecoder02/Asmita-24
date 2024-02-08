package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.Adapter.LeaderAdapter
import com.example.xenon.DataClass.ParticipateIIITS
import com.example.xenon.DataClass.TopDataClass
import com.example.xenon.databinding.FragmentLeaderBoardBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LeaderBoard : Fragment() {
    private lateinit var binding: FragmentLeaderBoardBinding
    private var user: MutableList<ParticipateIIITS> = mutableListOf()
    private var top3: MutableList<TopDataClass> = mutableListOf()
    private lateinit var useAdapter: LeaderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderBoardBinding.inflate(layoutInflater, container, false)
        binding.leaderRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useAdapter = LeaderAdapter(user)
        binding.leaderRv.layoutManager = LinearLayoutManager(context)
        binding.leaderRv.adapter = useAdapter
        fetchFromFirestore()
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.refresh.setOnRefreshListener {
            fetchFromFirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromFirestore() {
        user.clear()
        top3.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("IIITS").orderBy("Points", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                var count = 0 // Counter to track the top 3 records
                val top3List = mutableListOf<ParticipateIIITS>()
                val remainingList = mutableListOf<ParticipateIIITS>()
                for (document in documents) {
                    val name = document.getString("Name") ?: ""
                    val Logo = document.getString("logo") ?: ""
                    val Points = document.getLong("Points") ?: 0
                    val user = ParticipateIIITS(name, Logo, Points)
                    if (count < 3) {
                        top3List.add(user)
                    } else {
                        remainingList.add(user)
                    }
                    count++
                }
                top3.addAll(top3List.map { TopDataClass(it.Name, it.logo, it.Points) })
                user.addAll(remainingList)
                useAdapter.notifyDataSetChanged()

                if (top3.isNotEmpty()) {
                    binding.first.text = top3[0].Name
                    binding.firstScore.text = top3[0].Points.toString()
                    Glide.with(requireContext())
                        .load(top3[0].logo)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.firImg)
                }
                if (top3.size > 1) {
                    binding.second.text = top3[1].Name
                    binding.secondScore.text = top3[1].Points.toString()
                    Glide.with(requireContext())
                        .load(top3[1].logo)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.secImg)
                }
                if (top3.size > 2) {
                    binding.third.text = top3[2].Name
                    binding.thirdScore.text = top3[2].Points.toString()
                    Glide.with(requireContext())
                        .load(top3[2].logo)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.thiImg)
                }
                binding.resLot.visibility = View.INVISIBLE
                binding.leaderRv.visibility = View.VISIBLE
                binding.refresh.isRefreshing=false

            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}
