package com.example.xenon.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xenon.Activity.Main
import com.example.xenon.Adapter.Gallery2Adapter
import com.example.xenon.DataClass.Gallery2
import com.example.xenon.R
import com.example.xenon.databinding.FragmentGallery2Binding
import com.google.firebase.firestore.FirebaseFirestore
class Gallery2 : Fragment() {

private lateinit var binding:FragmentGallery2Binding
private var gall:MutableList<Gallery2> = mutableListOf()
    private lateinit var gallAdapter:Gallery2Adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentGallery2Binding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gallAdapter=Gallery2Adapter(gall,this)
        binding.sportsRv.adapter=gallAdapter
        binding.sportsRv.layoutManager=LinearLayoutManager(requireContext())

        fetchfromfirestore()
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
        ) // Use nextFragment instead of basefragmentevent()
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
