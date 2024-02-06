package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.DevAdapter
import com.example.xenon.DataClass.DeveloperDataClass
import com.example.xenon.DataClass.FixtureDataClass
import com.example.xenon.databinding.FragmentDeveloperBinding
import com.google.firebase.firestore.FirebaseFirestore

class Developer : Fragment() {
    private lateinit var binding:FragmentDeveloperBinding
    private val dev:MutableList<DeveloperDataClass> = mutableListOf()
    private lateinit var devAdapter: DevAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeveloperBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devAdapter = DevAdapter(dev)
        binding.devRv.adapter = devAdapter
        binding.devRv.layoutManager = LinearLayoutManager(requireContext())
        binding.menu.setOnClickListener {
            openDrawer()
        }
        fetch()
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }
    private fun fetch(){
        dev.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("Developer").orderBy("no").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val pos = document.getString("pos") ?: ""
                val image = document.getString("image") ?: ""
                val item = DeveloperDataClass(name,pos,image)
                dev.add(item)
            }
            devAdapter.notifyDataSetChanged()
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}