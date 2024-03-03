package com.interiiit.xenon.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.interiiit.xenon.Adapter.Fixture_Day_Adapter
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.R
import com.interiiit.xenon.databinding.FragmentFixtureDayWiseBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Fixture_Day_Wise : Fragment() {
    private lateinit var binding:FragmentFixtureDayWiseBinding
    private lateinit var dayAdapter:Fixture_Day_Adapter
    private val dayList:MutableList<Fixture_Day_DataClass> = mutableListOf()
    private var htmlString: String? = null
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
        binding.viewPager.adapter = DayPagerAdapter(dayList.map { it.data }, requireActivity())

        // Connect TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = dayList[position].day
        }.attach()

//        for (day in dayList) {
//            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(day.day))
//        }
//        binding.tabLayout.setTabTextColors(Color.WHITE,0xFFE9BE3E.toInt())
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
////                tab?.let {
////                    binding.viewPager.currentItem = it.position
////                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//        })
//        val webView: WebView = binding.root.findViewById(R.id.web)
//        webView.settings.javaScriptEnabled = true
//        htmlString?.let { webView.loadData(it, "text/html", "UTF-8") }
//
//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
//            }
//        })

        dayAdapter = Fixture_Day_Adapter(dayList, this)
        binding.dayRv.adapter = dayAdapter
        binding.dayRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val HTML_STRING_KEY = "html_string_key"
        fun newInstance(dayListJson: String): Fixture_Day_Wise {
            val fragment = Fixture_Day_Wise()
            val args = Bundle()
            args.putString("dayListJson", dayListJson)
            fragment.arguments = args
            return fragment
        }
    }
    class DayPagerAdapter(
        private val htmlStrings: List<String>,
        fragmentActivity: FragmentActivity
    ) : FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = htmlStrings.size

        override fun createFragment(position: Int): Fragment {
            return DayClassFragment.newInstance(htmlStrings[position])
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            htmlString = it.getString(HTML_STRING_KEY)
        }
    }
//    fun onItemClick(item: Fixture_Day_DataClass) {
//        val bundle = Bundle()
//        bundle.putString("name", item.day ?: "Name")
//        bundle.putString("data", item.data ?: "Date")
//        val nextFragment = Fixture()
//        nextFragment.arguments = bundle
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container, nextFragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
}