package com.glushko.sportcommunity.presentation_layer.ui.team.matches.results

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
import kotlinx.android.synthetic.main.fragment_match_results.*

class ResultsFragment: Fragment(),
    UpdateFragmentInterface {

    val layoutId: Int = R.layout.fragment_match_results
    lateinit var adapter: MatchesListAdapter

    companion object{
        fun newInstance(): ResultsFragment {
            return ResultsFragment()
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
        results_recycler.adapter = adapter
        results_recycler.layoutManager = LinearLayoutManager(activity)
    }

    override fun update(matches: List<MatchFootball>) {
        println("Сейчас все обновится результаты ! $matches")
        adapter.update(matches)
    }
}