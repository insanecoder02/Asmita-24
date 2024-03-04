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
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentFixtureDayWiseBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interiiit.xenon.Adapter.ButAdapter

class Fixture_Day_Wise : Fragment() {
    private lateinit var binding: FragmentFixtureDayWiseBinding
    private var dayList: MutableList<Fixture_Day_DataClass> = mutableListOf()
    private lateinit var butAdapter: ButAdapter
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
        dayList = Gson().fromJson(dayListJson, type)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        butAdapter = ButAdapter(dayList, this)
        binding.hor.adapter = butAdapter
        binding.hor.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        butAdapter.setSelectedPosition(0)
//        setupButtonClickListeners()
//        handleButtonClick(R.id.day1)
    }

    fun onButClick(item: Fixture_Day_DataClass) {
        val htmlContent = item.data
        binding.web.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }
//    fun
//    private fun setupButtonClickListeners() {
//        val buttons = listOf(
//            binding.day1,
//            binding.day2,
//            binding.day3,
//            binding.day4,
//            binding.day5,
//            binding.day6,
//            binding.day7
//        )
//
//        buttons.forEach { button ->
//            button.setOnClickListener {
//                handleButtonClick(button.id)
//                updateButtonBackground(buttons, button)
//            }
//        }
//    }
//    private fun updateButtonBackground(buttons: List<Button>, selectedButton: Button) {
//        buttons.forEach { button ->
//            if (button == selectedButton) {
//                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E9BD3E"))
//            } else {
//                button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
//            }
//        }
//    }
//    private fun handleButtonClick(buttonId: Int) {
//        val filteredList = when (buttonId) {
//            R.id.day1 -> dayList.filter { it.day == "1" }
//            R.id.day2 -> dayList.filter { it.day == "2" }
//            R.id.day3 -> dayList.filter { it.day == "3" }
//            R.id.day4 -> dayList.filter { it.day == "4" }
//            R.id.day5 -> dayList.filter { it.day == "5" }
//            R.id.day6 -> dayList.filter { it.day == "6" }
//            R.id.day7 -> dayList.filter { it.day == "7" }
//            else -> listOf()
//        }
//        if (filteredList.isEmpty()) {
//            binding.nore.visibility = View.VISIBLE
//            binding.web.visibility = View.INVISIBLE
//        } else {
//            binding.nore.visibility = View.GONE
//            binding.web.visibility = View.VISIBLE
//        }
//        displayHTMLContent(filteredList)
//    }
//    private fun displayHTMLContent(filteredList: List<Fixture_Day_DataClass>) {
//        if (filteredList.isNotEmpty()) {
//            val htmlContent = filteredList[0].data
//            binding.web.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
//        }
//    }
}