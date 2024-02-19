package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xenon.DataClass.Score.Matches
import com.example.xenon.R
import com.example.xenon.databinding.FragmentFixtureBinding
import com.example.xenon.databinding.FragmentResultSportBinding

class Result_Sport : Fragment() {
    private lateinit var binding: FragmentResultSportBinding
    private var reSport:MutableList<Matches> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultSportBinding.inflate(layoutInflater, container, false)
        requireActivity().window.statusBarColor = 0xFF000000.toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }
}