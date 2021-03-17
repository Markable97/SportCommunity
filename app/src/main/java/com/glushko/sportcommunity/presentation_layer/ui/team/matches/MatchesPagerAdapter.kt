package com.glushko.sportcommunity.presentation_layer.ui.team.matches

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class MatchesPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
       return when(position){
            0 -> ResultFragment.newInstance()
            1 -> CalendarFragment.newInstance()
            else -> ResultFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Результаты"
            1 -> "Календарь"
            else -> "Пусто"
        }
    }
}