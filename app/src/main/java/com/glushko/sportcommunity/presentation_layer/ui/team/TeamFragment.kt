package com.glushko.sportcommunity.presentation_layer.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R
import kotlinx.android.synthetic.main.activity_team_profile.*

class TeamFragment(teamName: String, teamDescription: String) : Fragment() {

    val layoutId: Int = R.layout.activity_team_profile

    val teamName = teamName
    val teamDescription = teamDescription
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        team_name.text = teamName
        team_description.text = teamDescription
    }
}