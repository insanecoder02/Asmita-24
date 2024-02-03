package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.xenon.databinding.FragmentSportDetailBinding

class sport_detail : Fragment() {
    private lateinit var binding: FragmentSportDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.name.text = name
        binding.date.text = date
        binding.location.text = location
        binding.heading.text = heading
        binding.discription.text = discription
        binding.length.text = length
        Glide.with(requireContext())
            .load(url)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.imageView6)
    }


}