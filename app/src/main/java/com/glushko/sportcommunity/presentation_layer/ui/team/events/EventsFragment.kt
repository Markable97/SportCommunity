package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel

class EventsFragment(private val team_id: Long, private val team_name: String): Fragment() {

    val layoutId: Int = R.layout.fragment_team_events

    lateinit var modelSquad: SquadViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelSquad = ViewModelProviders.of(this).get(SquadViewModel::class.java)
    }

}