package com.interiiit.xenon.Fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.interiiit.xenon.Adapter.ResultAdapter.ResultAdapter
import com.interiiit.xenon.DataClass.Score.MatchDetails
import com.interiiit.xenon.databinding.FragmentResultsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interiiit.xenon.R
import com.interiiit.xenon.other.IIITSlogo

class results : Fragment() {
    private lateinit var binding:FragmentResultsBinding
    private lateinit var resultAdapter: ResultAdapter
    private var logo = IIITSlogo.logo
    private lateinit var dayList: MutableList<MatchDetails>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultsBinding.inflate(layoutInflater, container, false)
        requireActivity().window.statusBarColor = 0xFF000000.toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")
        binding.text1.text = name
        val dayListJson = arguments?.getString("dayListJson")
        val type = object : TypeToken<List<MatchDetails>>() {}.type
        dayList = Gson().fromJson(dayListJson, type)

        resultAdapter = ResultAdapter(dayList, logo,Home(),false)
        binding.resultRv.adapter = resultAdapter
        binding.resultRv.layoutManager = LinearLayoutManager(requireContext())
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        setupButtonClickListeners()
        handleButtonClick(R.id.all)
    }

    private fun setupButtonClickListeners() {
        val buttons = listOf(
            binding.all,
            binding.day1,
            binding.day2,
            binding.day3,
            binding.day4,
            binding.day5,
            binding.day6,
            binding.day7
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                handleButtonClick(button.id)
                updateButtonBackground(buttons, button)
            }
        }
    }
    private fun updateButtonBackground(buttons: List<Button>, selectedButton: Button) {
        buttons.forEach { button ->
            if (button == selectedButton) {
                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E9BD3E"))
            } else {
                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
            }
        }
    }
    private fun handleButtonClick(buttonId: Int) {
        val filteredList = when (buttonId) {
            R.id.all -> dayList
            R.id.day1 -> dayList.filter { it.date == "9 Mar 2024" }
            R.id.day2 -> dayList.filter { it.date == "10 Mar 2024" }
            R.id.day3 -> dayList.filter { it.date == "11 Mar 2024" }
            R.id.day4 -> dayList.filter { it.date == "12 Mar 2024" }
            R.id.day5 -> dayList.filter { it.date == "13 Mar 2024" }
            R.id.day6 -> dayList.filter { it.date == "14 Mar 2024" }
            R.id.day7 -> dayList.filter { it.date == "15 Mar 2024" }
            else -> listOf()
        }
        if (filteredList.isEmpty()) {
            binding.nore.visibility = View.VISIBLE
            binding.resultRv.visibility = View.INVISIBLE
        } else {
            binding.nore.visibility = View.GONE
            binding.resultRv.visibility = View.VISIBLE
        }
        resultAdapter.updateData(filteredList)
    }
}