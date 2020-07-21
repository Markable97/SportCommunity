package com.glushko.sportcommunity.presentation_layer.ui.create_team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballDivisions
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballLeagues
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballTeams
import com.glushko.sportcommunity.presentation_layer.vm.InfoFootballTeamViewModel
import kotlinx.android.synthetic.main.fragment_info_football_team.*
import kotlinx.android.synthetic.main.spinner_item_info_teams.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFootballTeam.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFootballTeam : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val layoutId: Int = R.layout.fragment_info_football_team

    lateinit var model: InfoFootballTeamViewModel



    var leagues: MutableList<ResponseFootballLeagues.Params> = mutableListOf()
    var divisions: MutableList<ResponseFootballDivisions.Params> = mutableListOf()
    var teams: MutableList<ResponseFootballTeams.Params> = mutableListOf()
    lateinit var adapterLeagues: ArrayAdapter<ResponseFootballLeagues.Params>//CustomLeaguesAdapter
    lateinit var adapterDivisions: ArrayAdapter<ResponseFootballDivisions.Params>//CustomDivisionsAdapter
    lateinit var adapterTeams: ArrayAdapter<ResponseFootballTeams.Params>//CustomTeamsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
        adapterLeagues = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, leagues)//CustomLeaguesAdapter(requireContext(), leagues, R.layout.spinner_item_info_teams)
        adapterLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterDivisions = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, divisions)//CustomDivisionsAdapter(requireContext(), divisions)
        adapterDivisions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterTeams =  ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, teams)//CustomTeamsAdapter(requireContext(), teams)
        adapterTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        model = ViewModelProviders.of(this).get(InfoFootballTeamViewModel::class.java)
        model.getFootballLeagues()
        model.liveDataLeagues.observe(this, Observer {
            println("Live data 1 LEAGUES ${it.success} ${it.message} ${it.football_leagues}")
            if(it.success == 1){
                leagues = it.football_leagues
                adapterTeams.clear()
                adapterDivisions.clear()
                adapterLeagues.clear()
                adapterLeagues.addAll(leagues)
            }
        })
        model.liveDataDivisions.observe(this, Observer {
            println("Live data 2 DIVISIONS ${it.success} ${it.message} ${it.football_divisions}")
            if(it.success == 1){
                divisions.clear()
                divisions = it.football_divisions
                adapterDivisions.clear()
                adapterDivisions.addAll(divisions)
                sp_select_divisions.setSelection(0)
                model.getFootballTeams(divisions.first().division_id)
            }else{
                divisions.clear()
                adapterDivisions.clear()
            }
        })
        model.liveDataTeams.observe(this, Observer {
            println("Live data 3 Teams ${it.success} ${it.message} ${it.football_teams}")
            if(it.success == 1){
                teams = it.football_teams
                adapterTeams.clear()
                adapterTeams.addAll(teams)
                sp_select_team.setSelection(0)

            }else{
                teams.clear()
                adapterTeams.clear()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sp_select_league.adapter = adapterLeagues
        sp_select_league.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, itemSelect: View?, selectItemPosition: Int, selectedId: Long) {
                println("Выбрана лига - ${leagues[selectItemPosition]}")
                model.getFootballDivisions(leagues[selectItemPosition].league_id)
            }

        }
        sp_select_divisions.adapter = adapterDivisions
        sp_select_divisions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, itemSelect: View?, selectItemPosition: Int, selectedId: Long) {
                println("Выбран дивизион - ${divisions[selectItemPosition]}")
                model.getFootballTeams(divisions[selectItemPosition].division_id)
            }
        }
        sp_select_team.adapter = adapterTeams
        sp_select_team.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, itemSelect: View?, selectItemPosition: Int, selectedId: Long) {
                println("Выбрана команда - ${teams[selectItemPosition]}")

            }
        }
    }

    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFootballTeam.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFootballTeam()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}

