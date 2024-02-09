package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.xenon.Adapter.Fixture_Day_Adapter
import com.example.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.example.xenon.R
import com.example.xenon.databinding.FragmentFixtureDayWiseBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Fixture_Day_Wise : Fragment() {
    private lateinit var binding:FragmentFixtureDayWiseBinding
    private lateinit var dayAdapter:Fixture_Day_Adapter
    private val dayList:MutableList<Fixture_Day_DataClass> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixtureDayWiseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        binding.text1.text = name

        val dayListJson = arguments?.getString("dayListJson")
        val type = object : TypeToken<List<Fixture_Day_DataClass>>() {}.type
        val dayList: MutableList<Fixture_Day_DataClass> = Gson().fromJson(dayListJson, type)

        dayAdapter = Fixture_Day_Adapter(dayList, this)
        binding.dayRv.adapter = dayAdapter
        binding.dayRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    fun onItemClick(item: Fixture_Day_DataClass) {
        val bundle = Bundle()
        bundle.putString("name", item.day ?: "Name")
//        bundle.putString("date", item.date ?: "Date")
//        bundle.putString("image", item.image ?: "image")
//        bundle.putString("discription", item.discription ?: "Discription")
//        bundle.putString("heading", item.heading ?: "Heading")
//        bundle.putString("length", item.length ?: "Length")
//        bundle.putString("location", item.location ?: "Location")
//        bundle.putString("type", item.type ?: "Type")
        val nextFragment = Fixture()
        nextFragment.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}