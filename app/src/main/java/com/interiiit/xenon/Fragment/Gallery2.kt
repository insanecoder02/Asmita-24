package com.interiiit.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.interiiit.xenon.Activity.Main
import com.interiiit.xenon.Adapter.Gallery2Adapter
import com.interiiit.xenon.DataClass.GalleryDataClass.Gallery2
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentGallery2Binding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException

class Gallery2 : Fragment() {

private lateinit var binding:FragmentGallery2Binding
private var gall:MutableList<Gallery2> = mutableListOf()
    private lateinit var gallAdapter:Gallery2Adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGallery2Binding.inflate(layoutInflater,container,false)
        binding.sportsRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gallAdapter = Gallery2Adapter(gall,this)
        binding.sportsRv.adapter=gallAdapter
        binding.sportsRv.layoutManager=LinearLayoutManager(requireContext())
        binding.menu.setOnClickListener {
            openDrawer()
        }
        binding.refresh.setOnRefreshListener {
            fetchFromfirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }

        fetchFromfirestore()
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

    private fun fetchFromfirestore() {
        gall.clear()
        val url = "https://app-admin-api.asmitaiiita.org/api/gallery"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val documents = response.getJSONArray("data")
                    for (i in 0 until documents.length()) {
                        val document = documents.getJSONObject(i)
                        val imageurl = document.getString("image")
                        val title = document.getString("name")
                        val url = document.getString("url")
                        gall.add(Gallery2(title, imageurl, url))
                    }

                    gallAdapter.notifyDataSetChanged()
                    if (gall.isEmpty()) {
                        binding.t1.visibility = View.VISIBLE
                        binding.resLot.visibility = View.INVISIBLE
                        binding.sportsRv.visibility = View.INVISIBLE
                        binding.normal.visibility = View.INVISIBLE
                        binding.error.visibility = View.INVISIBLE
                    } else {
                        binding.t1.visibility = View.INVISIBLE
                        binding.resLot.visibility = View.INVISIBLE
                        binding.sportsRv.visibility = View.VISIBLE
                        binding.normal.visibility = View.VISIBLE
                        binding.error.visibility = View.INVISIBLE
                    }

                    binding.refresh.isRefreshing = false
                } catch (e: JSONException) {
                    handleNetworkError()
                    Toast.makeText(requireContext(), "Netwrok error", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                handleNetworkError()
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    private fun handleNetworkError() {
        binding.normal.visibility = View.INVISIBLE
        binding.error.visibility = View.VISIBLE
        binding.refresh.isRefreshing = false
        binding.loadBtn.setOnClickListener {
            fetchFromfirestore()
            binding.refresh.isRefreshing = true
        }
    }
}
