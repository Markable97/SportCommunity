package com.glushko.sportcommunity.presentation_layer.ui.team.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R

class CalendarFragment : Fragment(){

    val layoutId: Int = R.layout.fragment_match_calendar

    companion object{
        fun newInstance():CalendarFragment {
            return CalendarFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

}