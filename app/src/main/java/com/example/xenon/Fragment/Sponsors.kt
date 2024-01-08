package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.SponserAdapter
import com.example.xenon.DataClass.Sponser
import com.example.xenon.R
import com.example.xenon.databinding.FragmentSponsorsBinding
import com.google.firebase.firestore.FirebaseFirestore

class Sponsors : Fragment() {
    private lateinit var binding:FragmentSponsorsBinding
    private val spon:MutableList<Sponser> = mutableListOf()
    private lateinit var sponsAdapter: SponserAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSponsorsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sponsAdapter = SponserAdapter(requireContext(),spon)
        binding.sponsRV.adapter = sponsAdapter
        binding.sponsRV.layoutManager = LinearLayoutManager(requireContext())
        fetchFromFirestore()
    }

    private fun fetchFromFirestore() {
        spon.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Sponser").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val item = Sponser(name)
                spon.add(item)

            }
            sponsAdapter.notifyDataSetChanged()

        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}