package com.interiiit.xenon.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.interiiit.xenon.databinding.FragmentGalleryBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.interiiit.xenon.Adapter.Team.GalleryFbAdapter
import com.interiiit.xenon.DataClass.GalleryDataClass.GalleryFb

class Gallery : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var galfbAdapter: GalleryFbAdapter
    private val fbgal: MutableList<GalleryFb> = mutableListOf()
    private val storageRef: StorageReference =
        FirebaseStorage.getInstance().reference.child("gallery")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(layoutInflater, container, false)
        binding.gallRV.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url")
        val name = arguments?.getString("details")

        galfbAdapter = GalleryFbAdapter(requireContext(), fbgal)
        binding.gallRV.adapter = galfbAdapter
        binding.gallRV.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        storageRef.child(name!!).listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { imageRef ->
                    imageRef.downloadUrl.addOnSuccessListener { url ->
                        fbgal.add(GalleryFb(url.toString()))
                        galfbAdapter.notifyDataSetChanged()
                    }
                }
                binding.gallRV.visibility = View.VISIBLE
                binding.resLot.visibility = View.INVISIBLE
            }
            .addOnFailureListener { exception ->

            }

        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.text1.text = name
        binding.loadBtn.setOnClickListener {
            if (!url.isNullOrBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                Snackbar.make(binding.root, "Link is not Available", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}