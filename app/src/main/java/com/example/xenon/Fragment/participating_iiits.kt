package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xenon.DataClass.ParticipateIIITS
import com.example.xenon.databinding.FragmentParticipateBinding
import com.google.firebase.firestore.FirebaseFirestore
import ParticipatingAdapter

class participating_iiits : Fragment() {
    private lateinit var binding:FragmentParticipateBinding
    private var iiits: MutableList<ParticipateIIITS> = mutableListOf()
    private lateinit var partAdapter: ParticipatingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParticipateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        partAdapter = ParticipatingAdapter(iiits)
        binding.playingIits.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.playingIits.adapter = partAdapter
        fetchFromFirestore()
    }
    private fun fetchFromFirestore() {
        iiits.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("IIITS").orderBy("Name").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("Name") ?: ""
                val Logo = document.getString("logo") ?: ""
                val point = document.getLong("point") ?: 0
                iiits.add(ParticipateIIITS(name,Logo,point))
            }
            partAdapter.notifyDataSetChanged()
        }.addOnFailureListener{ e->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}