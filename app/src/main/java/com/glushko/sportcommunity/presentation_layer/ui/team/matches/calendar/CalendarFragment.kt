package com.glushko.sportcommunity.presentation_layer.ui.team.matches.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.MatchFootball
import com.glushko.sportcommunity.presentation_layer.ui.team.matches.MatchesListAdapter
import com.glushko.sportcommunity.presentation_layer.ui.team.matches.UpdateFragmentInterface
import kotlinx.android.synthetic.main.fragment_match_calendar.*

class CalendarFragment : Fragment(),
    UpdateFragmentInterface {

    val layoutId: Int = R.layout.fragment_match_calendar
    lateinit var adapter: MatchesListAdapter
    companion object{
        fun newInstance(): CalendarFragment {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchesListAdapter(callback = object : MatchesListAdapter.Callback{
            override fun onItemResult(match_id: Long) {
                println("Переход на матч")
            }

        })
        calendar_recycler.adapter = adapter
        calendar_recycler.layoutManager = LinearLayoutManager(activity)
    }

    override fun update(matches: List<MatchFootball>) {
        println("Сейчас все обновится календарь ! $matches")
        adapter.update(matches)
    }

}