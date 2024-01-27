package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.xenon.R
import com.example.xenon.databinding.FragmentEventBinding
import com.example.xenon.databinding.FragmentSponsorsBinding
import com.example.xenon.databinding.FragmentSportDetailBinding


class sport_detail : Fragment() {

    private lateinit var binding: FragmentSportDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSportDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        val date = arguments?.getString("date")
        val url = arguments?.getString("image")
        val heading = arguments?.getString("heading")
        val location = arguments?.getString("location")
        val discription = arguments?.getString("discription")
        val length = arguments?.getString("length")

        binding.name.text = name
        binding.date.text = date
        binding.location.text = location
        binding.heading.text = heading
        binding.discription.text = discription
        binding.length.text = length
        Glide.with(requireContext()).load(url).into(binding.image)
    }



}