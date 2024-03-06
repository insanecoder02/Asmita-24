package com.interiiit.xenon.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.databinding.FragmentFixtureUpcomingBinding

class Fixture_Upcoming : Fragment() {
    private lateinit var binding: FragmentFixtureUpcomingBinding
    private var dayList: MutableList<Fixture_Day_DataClass> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixtureUpcomingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")
        val dayListJson = arguments?.getString("dayListJson")
        val type = object : TypeToken<List<Fixture_Day_DataClass>>() {}.type
        dayList = Gson().fromJson(dayListJson, type)
        binding.text1.text = name+" - ${dayList[0].day}"
                binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        val htmlContent = buildString {
            append("<html><head><style>\n")
            append("    * {\n")
            append("        background: #1e1e1e;\n")
            append("        color: #fff;\n")
            append("    }\n")
            append("    tr:first-of-type td {\n")
            append("        color: #e9bd3e;\n")
            append("    }\n")
            append("</style></head><body>\n")

            for (day in dayList) {
                append(day.data)
            }
            append("</body></html>")
        }
        binding.web.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }
}