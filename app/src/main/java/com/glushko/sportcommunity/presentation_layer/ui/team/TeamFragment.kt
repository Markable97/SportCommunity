package com.glushko.sportcommunity.presentation_layer.ui.team

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R
import kotlinx.android.synthetic.main.activity_team_profile.*

class TeamFragment(teamName: String, teamDescription: String, bitmap: Bitmap, private val leaderId: Long, private val leaderName: String,
                   private val isLeader: Boolean, val callbackActivity: Callback) : Fragment() {

    val layoutId: Int = R.layout.activity_team_profile
    val bitmap: Bitmap = bitmap
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
        team_image_profile.setImageBitmap(bitmap)
        team_name.text = teamName
        team_description.text = teamDescription
        if(!isLeader){
            team_profile_btn_2.text = getString(R.string.team_profile_btn_2_dop)
        }

        team_profile_btn_2.setOnClickListener {
            if(!isLeader){
                callbackActivity.onClickUpperRightButton(leaderId, leaderName)
            }
        }

    }

    interface Callback{
        fun onClickUpperRightButton(idLeader: Long, leaderName: String)

    }
}