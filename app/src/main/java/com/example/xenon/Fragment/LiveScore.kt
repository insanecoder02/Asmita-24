package com.example.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenon.Adapter.MatchAdapter
import com.example.xenon.DataClass.Matches
import com.example.xenon.R
import com.example.xenon.databinding.FragmentHomeBinding
import com.example.xenon.databinding.FragmentLiveScoreBinding

class LiveScore : Fragment() {
    private lateinit var binding: FragmentLiveScoreBinding
    private lateinit var adapter: MatchAdapter
    private var mat:MutableList<Matches> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveScoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchAdapter(requireContext(),mat)
        binding.matchRV.adapter = adapter
        binding.matchRV.layoutManager = LinearLayoutManager(requireContext())

    }
}