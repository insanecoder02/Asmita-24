package com.interiiit.xenon.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.interiiit.xenon.Adapter.FragmentAdapter
import com.interiiit.xenon.databinding.FragmentLeaderBoardBinding
import com.google.android.material.tabs.TabLayout
import com.interiiit.xenon.Activity.Main

class LeaderBoard : Fragment() {
    private lateinit var binding: FragmentLeaderBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Student"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Employee"))

        binding.viewPager.adapter = FragmentAdapter(lifecycle,requireActivity().supportFragmentManager)

        binding.back.setOnClickListener {
            openDrawer()
        }
        binding.tabLayout.setTabTextColors(Color.WHITE,0xFFE9BE3E.toInt())
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.viewPager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun openDrawer() {
        val mainActivity = requireActivity() as Main
        mainActivity.openDrawer()
    }
}
