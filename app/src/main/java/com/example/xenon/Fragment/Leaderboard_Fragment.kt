package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.Adapter.UserAdapter
import com.example.xenon.DataClass.users
import com.example.xenon.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Leaderboard_Fragment : Fragment() {
    private lateinit var usrs: MutableList<users>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter // Assuming you have an adapter for your RecyclerView

    private lateinit var firstName: TextView
    private lateinit var firstScore: TextView
    private lateinit var firstImage: ImageView

    private lateinit var secondName: TextView
    private lateinit var secondScore: TextView
    private lateinit var secondImage: ImageView

    private lateinit var thirdName: TextView
    private lateinit var thirdScore: TextView
    private lateinit var thirdImage: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leaderboard_, container, false)

        recyclerView = view.findViewById(R.id.Leaderboard_rv) // Replace with your RecyclerView id
        usrs = mutableListOf()
        adapter = UserAdapter(usrs, requireContext()) // Replace with your adapter initialization

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        firstImage = view.findViewById(R.id.firstImage)
        secondImage = view.findViewById(R.id.secondImage)
        thirdImage = view.findViewById(R.id.thirdImage)
        firstName = view.findViewById(R.id.firstName)
        secondName = view.findViewById(R.id.secondName)
        thirdName = view.findViewById(R.id.thirdName)
        firstScore = view.findViewById(R.id.firstScore)
        secondScore = view.findViewById(R.id.secondScore)
        thirdScore = view.findViewById(R.id.thirdScore)

        fetchFromFirestore()

        return view
    }

    private fun fetchFromFirestore() {
        usrs.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("IIITS").orderBy("Points", Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("Name") ?: ""
                val logo = document.getString("logo") ?: ""
                val points = document.getLong("Points") ?: 0
                usrs.add(users(name, logo, points))
            }

            val top3Name = arrayOf(firstName, secondName, thirdName)
            val top3Image = arrayOf(firstImage, secondImage, thirdImage)
            val top3Score = arrayOf(firstScore, secondScore, thirdScore)

            for (i in 0 until usrs.size.coerceAtMost(3)) {
                top3Name[i].text = usrs[i].Name
                top3Score[i].text = usrs[i].Points.toString()
                Glide.with(requireContext())
                    .load(usrs[i].logo)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(top3Image[i])
            }

            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
        }
    }

}
