package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.Gallery2Adapter
import com.example.xenon.DataClass.GalleryDataClass.Gallery2
import com.example.xenon.R
import com.example.xenon.databinding.FragmentGallery2Binding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
class Gallery2 : Fragment() {

private lateinit var binding:FragmentGallery2Binding
private var gall:MutableList<Gallery2> = mutableListOf()
    private lateinit var gallAdapter:Gallery2Adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentGallery2Binding.inflate(layoutInflater,container,false)
        binding.sportsRv.visibility = View.INVISIBLE
        binding.resLot.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gallAdapter=Gallery2Adapter(gall,this)
        binding.sportsRv.adapter=gallAdapter
        binding.sportsRv.layoutManager=LinearLayoutManager(requireContext())
        binding.menu.setOnClickListener {
            openDrawer()
        }
        binding.refresh.setOnRefreshListener {
            fetchfromfirestore()
            Snackbar.make(binding.root, "Data refreshed", Snackbar.LENGTH_SHORT).show()
        }

        fetchfromfirestore()
    }
    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }

    private fun fetchfromfirestore() {
        gall.clear()
        val db=FirebaseFirestore.getInstance()
        db.collection("Gallery").get().addOnSuccessListener {documents->
//            val list= mutableListOf<Gallery2>()
            for(document in documents){
                val imageurl=document.getString("image")?:""
                val title=document.getString("name")?:""
                val url=document.getString("url")?:""
                gall.add(Gallery2(title,imageurl,url))
            }
            gallAdapter.notifyDataSetChanged()
            binding.resLot.visibility = View.INVISIBLE
            binding.sportsRv.visibility = View.VISIBLE
            binding.refresh.isRefreshing=false
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
    fun onItemClick(item: Gallery2) {
//        Log.d(
//            "Events",
//            "Date: ${item.date}, Details: ${item.details}, Form: ${item.form}, Name: ${item.name}, No: ${item.no}, Time: ${item.time}, URL: ${item.url}, Venue: ${item.venue}"
//        )
        val bundle = Bundle()
        bundle.putString("date", item.sport_img ?: "Date")
        bundle.putString("details", item.sport_name)
        bundle.putString("url", item.url)
//        bundle.putString("form", item.form ?: "Form")
//        bundle.putString("name", item.name ?: "Name")
//        bundle.putLong("no", item.no ?: 123)
//        bundle.putString("time", item.time ?: "Time")
//        bundle.putString("url", item.url ?: "Url")
//        bundle.putString("venue", item.venue ?: "Venue")
        val nextFragment = Gallery()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragment_container, nextFragment
        )
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
