package com.example.xenon.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.AboutAdapter
import com.example.xenon.DataClass.AboutUs
import com.example.xenon.R
import com.example.xenon.databinding.FragmentAboutUsBinding
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.firestore.FirebaseFirestore

class AboutUs : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding
    private val abtus: MutableList<AboutUs> = mutableListOf()
    private lateinit var abtAdapter: AboutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)
        binding.aboutRV.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        abtAdapter = AboutAdapter(abtus)
        binding.aboutRV.adapter = abtAdapter
        binding.aboutRV.layoutManager = LinearLayoutManager(requireContext())
        fetchFromFirestore()
    }

    private fun fetchFromFirestore() {
        abtus.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("AboutUs").orderBy("no").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("info") ?: ""
                val image = document.getString("image") ?: ""
                val item = AboutUs(name, image)
                abtus.add(item)
            }
            abtAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.aboutRV.visibility = View.VISIBLE
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}