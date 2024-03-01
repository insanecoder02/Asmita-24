package com.interiiit.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.interiiit.xenon.databinding.FragmentFixtureBinding

class Fixture : Fragment() {
    private lateinit var binding: FragmentFixtureBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixtureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")
        binding.text1.text = name
        val htmlContent = arguments?.getString("data")
        val styledHtml = "<style>body { font-size: 16dp; }</style>$htmlContent"

        binding.web.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}