package com.interiiit.xenon.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.interiiit.xenon.DataClass.FixtureDataClass.Fixture_Day_DataClass
import com.interiiit.xenon.Fragment.Fixture_Day_Wise

class FixtureDayWIseAdapter(private val dayHtmlList: List<Fixture_Day_DataClass>,
                            fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int = dayHtmlList.size

    override fun getItem(position: Int): Fragment {
        return Fixture_Day_Wise.newInstance(dayHtmlList[position].data)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return dayHtmlList[position].day
    }
}