package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.GalleryAdapter
import com.example.xenon.DataClass.Gallery
import com.example.xenon.databinding.FragmentGalleryBinding

class Gallery : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var galAdapter: GalleryAdapter
    private val gal: MutableList<Gallery> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galAdapter = GalleryAdapter(requireContext(), gal)
        binding.gallRV.adapter = galAdapter
        binding.gallRV.layoutManager = LinearLayoutManager(requireContext())

        fetchFromFirestore()
    }

    private fun fetchFromFirestore() {

    }
}