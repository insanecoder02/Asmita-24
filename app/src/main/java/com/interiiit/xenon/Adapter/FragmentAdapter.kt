package com.interiiit.xenon.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.interiiit.xenon.Fragment.LeaderBoard_Empl
import com.interiiit.xenon.Fragment.LeaderBoard_Stud

class FragmentAdapter(
    lifecycle: Lifecycle,
    fragmentManager: FragmentManager
) : FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position==0){
            LeaderBoard_Stud()
        }
        else{
            LeaderBoard_Empl()
        }
    }

}