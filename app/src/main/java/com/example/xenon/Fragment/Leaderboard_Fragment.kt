package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.Adapter.UserAdapter
import com.example.xenon.DataClass.AboutUs
import com.example.xenon.DataClass.users
import com.example.xenon.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.nio.file.attribute.UserPrincipalLookupService

class Leaderboard_Fragment : Fragment() {
    private lateinit var usrs: MutableList<users>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter // Assuming you have an adapter for your RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_leaderboard_, container, false)

        recyclerView = view.findViewById(R.id.Leaderboard_rv) // Replace with your RecyclerView id
        usrs= mutableListOf()
        adapter = UserAdapter(usrs,requireContext()) // Replace with your adapter initialization

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        fetchFromFirestore()

        return view
    }

    private fun fetchFromFirestore() {
        usrs.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("IIITS").orderBy("Points").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("Name") ?: ""
                val Logo = document.getString("logo") ?: ""
                val Points = document.getLong("Points") ?: 0
                usrs.add(users(name,Logo,Points))
            }
            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
        }
    }
}
