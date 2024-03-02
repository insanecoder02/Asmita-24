package com.interiiit.xenon.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.interiiit.xenon.Adapter.GalleryAdapter
import com.interiiit.xenon.DataClass.GalleryDataClass.FlickrPhoto
import com.interiiit.xenon.databinding.FragmentGalleryBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.interiiit.xenon.Adapter.Team.GalleryFbAdapter
import com.interiiit.xenon.DataClass.GalleryDataClass.GalleryFb

class Gallery : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var galAdapter: GalleryAdapter
    private lateinit var galfbAdapter: GalleryFbAdapter
    private val gal: MutableList<FlickrPhoto> = mutableListOf()
    private val fbgal : MutableList<GalleryFb> = mutableListOf()
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("gallery")
    private val api_key = "c04b69fd41509ef0642390c428f22081"

    //getting firebase storage



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGalleryBinding.inflate(layoutInflater, container, false)
        binding.gallRV.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        binding.loadBtn.visibility = View.INVISIBLE






        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("url")
        val name = arguments?.getString("details")

        storageRef.child(name.toString())
        galfbAdapter = GalleryFbAdapter(requireContext(), fbgal)
        binding.gallRV.adapter = galfbAdapter
        binding.gallRV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.items.forEach { imageRef ->
                    imageRef.downloadUrl.addOnSuccessListener { url ->
                        fbgal.add(url.toString())
                        galAdapter.notifyDataSetChanged()
                        Toast.makeText(context, "hi url $url", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
            }



//        val flickrService = RetrofitClient.create()

//        flickrService.getPhotos(apiKey = api_key)
//            .enqueue(object : Callback<FlickrResponse> {
//                override fun onResponse(
//                    call: Call<FlickrResponse>,
//                    response: Response<FlickrResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val photos = response.body()?.photos?.photo
//
//                        photos?.forEach { photo ->
//                            val imageUrl =
//                                "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_m.jpg"
//                            gal.add(FlickrPhoto(photo.id, photo.farm, photo.server, photo.secret))
//                        }
//                        galAdapter.notifyDataSetChanged()
//                        binding.resLot.visibility = View.INVISIBLE
//                        binding.gallRV.visibility = View.VISIBLE
//                        binding.loadBtn.visibility = View.VISIBLE
//                    }
//                }
//
//                override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
////                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
//                }
//            })



        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.text1.text = name
        binding.loadBtn.setOnClickListener {
            if (!url.isNullOrBlank()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                val snackbar = Snackbar.make(
                    binding.root,
                    "Link is not Available",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
        }
//        fetchFromFirestore()

    }

}