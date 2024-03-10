package com.interiiit.xenon.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
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
    }

    fun onButClick(item: Fixture_Day_DataClass) {
        val htmlContent = item.data+"<style>\n" +
                "    * {\n" +
                "        background: #1e1e1e;\n" +
                "        color: #fff;\n" +
                "    }\n" +
                "\n" +
                "    tr:first-of-type * {\n" +
                "        color: #e9bd3e;\n" +
                "    }\n" +
                "</style>"
        binding.web.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }
}