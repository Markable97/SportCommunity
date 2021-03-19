package com.glushko.sportcommunity.presentation_layer.ui.team.matches

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.glushko.sportcommunity.business_logic_layer.domain.MatchFootball
import com.glushko.sportcommunity.presentation_layer.ui.team.matches.calendar.CalendarFragment
import com.glushko.sportcommunity.presentation_layer.ui.team.matches.results.ResultsFragment

class MatchesPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var list: MutableList<MatchFootball> = mutableListOf()

    override fun getItem(position: Int): Fragment {
       return when(position){
            0 -> ResultsFragment.newInstance()
            1 -> CalendarFragment.newInstance()
            else -> ResultsFragment.newInstance()
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

    override fun getItemPosition(`object`: Any): Int {
        println("Обновим фрагменты TabLayout")
        when(`object`){
            is ResultsFragment -> {
                `object`.update(list.filter { it.played == 1 })
            }
            is CalendarFragment -> {
                `object`.update(list.filter { it.played == 0 })
            }
        }
        return super.getItemPosition(`object`)
    }

    fun updateMatchesFragment(matches: MutableList<MatchFootball>){
        list = matches
        notifyDataSetChanged()
    }
}