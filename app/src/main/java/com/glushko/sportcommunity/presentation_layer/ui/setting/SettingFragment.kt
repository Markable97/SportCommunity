package com.glushko.sportcommunity.presentation_layer.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.ui.create_team.CreateTeamActivity
import com.glushko.sportcommunity.presentation_layer.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    val layoutId = R.layout.fragment_setting

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCreateTeam.setOnClickListener{
            val intent = Intent(activity, CreateTeamActivity::class.java)
            activity?.startActivity(intent)
        }
    }
}