package com.example.xenon.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xenon.Adapter.GalleryAdapter
import com.example.xenon.DataClass.GalleryDataClass.FlickrPhoto
import com.example.xenon.DataClass.GalleryDataClass.FlickrResponse
import com.example.xenon.databinding.FragmentGalleryBinding
import com.example.xenon.other.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Gallery : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var galAdapter: GalleryAdapter
    private val gal: MutableList<FlickrPhoto> = mutableListOf()
    private val api_key = "c04b69fd41509ef0642390c428f22081"
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
        binding.gallRV.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        val flickrService = RetrofitClient.create()

        flickrService.getPhotos(apiKey = api_key)
            .enqueue(object : Callback<FlickrResponse> {
                override fun onResponse(
                    call: Call<FlickrResponse>,
                    response: Response<FlickrResponse>
                ) {
                    if (response.isSuccessful) {
                        val photos = response.body()?.photos?.photo

                        photos?.forEach { photo ->
                            val imageUrl =
                                "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_m.jpg"
                            gal.add(FlickrPhoto(photo.id, photo.farm, photo.server, photo.secret))
                        }
                        galAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        val url = arguments?.getString("url")
        val name = arguments?.getString("details")
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

    private fun fetchFromFirestore() {

    }
}