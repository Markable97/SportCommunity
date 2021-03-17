package com.glushko.sportcommunity.presentation_layer.ui.team.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.vm.InfoFootballTeamViewModel
import kotlinx.android.synthetic.main.fragment_team_matches.*


class MatchesFragment: Fragment() {

    val layoutId: Int = R.layout.fragment_team_matches
    lateinit var model: InfoFootballTeamViewModel
    companion object{

        const val TAG = "KEY_MATCHES_FOOTBALL"
        const val KEY1 = "team_id"

        fun newInstance(team_id: Long): MatchesFragment {
            return MatchesFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY1, team_id)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(InfoFootballTeamViewModel::class.java)
        model.liveDataMatches.observe(this, Observer {
            if(it.success == 1){
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })

        arguments?.let {
            model.getFootballMatchesTeam(team_id = it.getLong(MatchesFragment.KEY1))
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
        val adapter = MatchesPagerAdapter(parentFragmentManager)
        viewPagerMatches.adapter = adapter
        sliding_tabs_for_matches.setupWithViewPager(viewPagerMatches)
    }


}