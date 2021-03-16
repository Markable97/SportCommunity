package com.glushko.sportcommunity.presentation_layer.ui.team.tournament_table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.presentation_layer.vm.InfoFootballTeamViewModel

abstract class TournamentTableFragment: Fragment() {

    abstract val layoutId: Int

    lateinit var model: InfoFootballTeamViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(InfoFootballTeamViewModel::class.java)

    }

}