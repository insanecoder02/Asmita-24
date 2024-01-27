package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.Adapter.UserAdapter
import com.example.xenon.DataClass.IIITs
import com.example.xenon.DataClass.users
import com.example.xenon.R
import com.google.firebase.firestore.FirebaseFirestore
import showpageAdapter


class participating_iiits : Fragment() {
    private lateinit var iiits: MutableList<IIITs>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: showpageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_participating_iiits, container, false)

        recyclerView= view.findViewById(R.id.playing_iits) // Replace with your RecyclerView id
        iiits= mutableListOf()
        adapter = showpageAdapter( requireContext() , iiits) // Replace with your adapter initialization





        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = adapter

        fetchFromFirestore()

        return view
    }

    private fun fetchFromFirestore() {
        iiits.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("IIITS").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("Name") ?: ""
                val Logo = document.getString("logo") ?: ""
                iiits.add(IIITs(name,Logo))
            }
            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
            showToast("Data fetched succesfully")
        }.addOnFailureListener{ e->
            showToast("failed to fetch data")

        }
    }

    private fun showToast(s: String){
        Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show()

    }
}