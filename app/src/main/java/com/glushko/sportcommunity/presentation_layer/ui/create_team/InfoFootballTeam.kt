package com.glushko.sportcommunity.presentation_layer.ui.create_team

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
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

private const val RQ_CODE = 1
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

    //lateinit var adapter: CustomAdapter

    var action: String = "Start"
    var idTeam: Int? = null
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
        //adapter = CustomAdapter(requireContext(),  leagues, android.R.layout.simple_spinner_item)
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLeagues = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, leagues)//CustomLeaguesAdapter(requireContext(), leagues, R.layout.spinner_item_info_teams)
        adapterLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterDivisions = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, divisions)//CustomDivisionsAdapter(requireContext(), divisions)
        adapterDivisions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterTeams =  ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, teams)//CustomTeamsAdapter(requireContext(), teams)
        adapterTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        model = ViewModelProviders.of(this).get(InfoFootballTeamViewModel::class.java)
        model.liveDataLeagues.observe(this, Observer {
            println("Live data 1 LEAGUES ${it.success} ${it.message} ${it.football_leagues}")
            if(it.success == 1){
                tv_select_league.visibility = View.VISIBLE
                leagues = it.football_leagues
                tv_info_choose.text = "Выберите лигу"
                action = "Select league"
            }else{
                leagues.clear()
            }
        })
        model.liveDataDivisions.observe(this, Observer {
            println("Live data 2 DIVISIONS ${it.success} ${it.message} ${it.football_divisions}")
            if(it.success == 1){
                tv_select_division.visibility = View.VISIBLE
                divisions = it.football_divisions
                tv_info_choose.text = "Выберите дивизион"
                action = "Select division"
            }
        })
        model.liveDataTeams.observe(this, Observer {
            println("Live data 3 Teams ${it.success} ${it.message} ${it.football_teams}")
            if(it.success == 1){
                tv_select_team.visibility = View.VISIBLE
                teams = it.football_teams
                tv_info_choose.text = "Выберите команду"
                action = "Select team"
            }else{
                teams.clear()
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
        tv_info_choose.setOnClickListener {
            when(action){
                "Start" -> {
                    model.getFootballLeagues()
                }
                "Select league" ->{
                    val dialog = InfoFootballTeamDialog("Лигу", leagues.toList().map(
                        ResponseFootballLeagues.Params::league_name
                    ).toTypedArray())
                    dialog.setTargetFragment(this, RQ_CODE)
                    fragmentManager.let {
                        dialog.show(it!!, dialog.javaClass.name)
                    }
                }
                "Select division" ->{
                    val dialog = InfoFootballTeamDialog("Дивизион", divisions.toList().map(
                        ResponseFootballDivisions.Params::division_name
                    ).toTypedArray())
                    dialog.setTargetFragment(this, RQ_CODE)
                    fragmentManager.let {
                        dialog.show(it!!, dialog.javaClass.name)
                    }
                }
                "Select team" ->{
                    val dialog = InfoFootballTeamDialog("Команду", teams.toList().map(
                        ResponseFootballTeams.Params::team_name
                    ).toTypedArray())
                    dialog.setTargetFragment(this, RQ_CODE)
                    fragmentManager.let {
                        dialog.show(it!!, dialog.javaClass.name)
                    }
                }
                "Success" ->{
                    /*Toast.makeText(activity, "Коздаем команду", Toast.LENGTH_LONG).show()
                    val animator = ObjectAnimator.ofInt(circularProgressBar, "progress", 0, 100)
                    animator.interpolator = DecelerateInterpolator(2F)
                    animator.duration = 5000
                    animator.start()
                    */
                    circularProgressBar.indeterminateMode = true

                }
            }

        }
        sp_select_league.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, itemSelect: View?, selectItemPosition: Int, selectedId: Long) {
                println("Выбрана лига в спинере- ${leagues[selectItemPosition]}")
                circularProgressBar.setProgressWithAnimation(1f, 1000)
                model.getFootballDivisions(leagues[selectItemPosition].league_id)
                sp_select_divisions.visibility = View.INVISIBLE
                sp_select_team.visibility = View.INVISIBLE

            }
        }
        sp_select_divisions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, itemSelect: View?, selectItemPosition: Int, selectedId: Long) {
                println("Выбран дивизион в спинере - ${divisions[selectItemPosition]}")
                circularProgressBar.setProgressWithAnimation(2f, 1000)
                model.getFootballTeams(divisions[selectItemPosition].division_id)
                sp_select_team.visibility = View.INVISIBLE

            }
        }
        sp_select_team.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, itemSelect: View?, selectItemPosition: Int, selectedId: Long) {
                println("Выбрана команда в спинере- ${teams[selectItemPosition]}")
                circularProgressBar.setProgressWithAnimation(3f, 1000)
                idTeam = teams[selectItemPosition].team_id
            }
        }
    }

    private fun updateUI(position: Int){

        when( action){
            "Select league" -> {
                sp_select_league.visibility = View.VISIBLE

                adapterLeagues = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, leagues)
                adapterLeagues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_select_league.adapter = adapterLeagues
                sp_select_league.setSelection(position)
            }
            "Select division" ->{
                sp_select_divisions.visibility = View.VISIBLE

                adapterDivisions = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, divisions)//CustomDivisionsAdapter(requireContext(), divisions)
                adapterDivisions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp_select_divisions.adapter = adapterDivisions
                sp_select_divisions.setSelection(position)
            }
            "Select team" ->{
                sp_select_team.visibility = View.VISIBLE
                adapterTeams = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, teams)//CustomDivisionsAdapter(requireContext(), divisions)
                adapterTeams.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                sp_select_team.adapter = adapterTeams
                sp_select_team.setSelection(position)
                action = "Success"
                tv_info_choose.text = "Завершить создание!"
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode ){
                RQ_CODE -> {
                    val position = data?.getIntExtra(InfoFootballTeamDialog.TAG_ID, -1)
                    println("Ответ от диалога позиция ${position}")
                    if(position != null){
                        updateUI(position)
                    }
                }
            }
        }
    }

    //CircularProgressBar
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

