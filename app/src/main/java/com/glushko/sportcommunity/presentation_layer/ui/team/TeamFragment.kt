package com.glushko.sportcommunity.presentation_layer.ui.team

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.ui.team.events.CreateEventDialog
import kotlinx.android.synthetic.main.activity_team_profile.*

class TeamFragment() : Fragment() {

    private var leaderName: String? = null
    private var leaderId: Long? = null
    private var isLeader: Boolean? = null
    private var teamId: Int? = null
    var callbackActivity: Callback? = null
    constructor(teamName: String, teamDescription: String, bitmap: Bitmap, leaderId: Long, leaderName: String,
                isLeader: Boolean, teamId: Int, callbackActivity: Callback) : this() {
        this.leaderName = leaderName
        this.leaderId = leaderId
        this.teamName = teamName
        this.teamDescription = teamDescription
        this.bitmap = bitmap
        this.isLeader = isLeader
        this.teamId = teamId
        this.callbackActivity = callbackActivity

    }

    val layoutId: Int = R.layout.activity_team_profile
    private var bitmap: Bitmap? = null
    var teamName: String? = null
    private var teamDescription: String? = null

    companion object{
        const val TAG = "KEY_TEAM"
        const val KEY1 = "team_id"
        fun newInstance(team_id: Long): TeamFragment{
            return TeamFragment().apply {
                arguments = Bundle().apply {
                    putLong(CreateEventDialog.KEY1, team_id)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            this.teamId = it.getLong(KEY1).toInt()
        }
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        team_image_profile.setImageBitmap(bitmap)
        team_name.text = teamName
        team_description.text = teamDescription
        isLeader?.let {
            if(!it){
                team_profile_btn_2.text = getString(R.string.team_profile_btn_2_dop)
            }
        }


        team_profile_btn_2.setOnClickListener {
            isLeader?.let {
                if(!it){
                    callbackActivity?.onClickUpperRightButton(leaderId!!, leaderName!!)
                }
            }
        }

        team_profile_btn_chat.setOnClickListener {
            callbackActivity?.onClickUpperLeftButton(teamName!!, teamId!!)
        }

        btn_team_squad.setOnClickListener {
            callbackActivity?.onClickSquad(teamName!!, isLeader!!)
        }

        btn_squad_events.setOnClickListener {
            callbackActivity?.onClickEvents(teamId?.toLong()!!, teamName!!, isLeader!!)
        }

    }

    interface Callback{
        fun onClickUpperRightButton(idLeader: Long, leaderName: String)
        fun onClickSquad(team_name: String, isLeader: Boolean)
        fun onClickUpperLeftButton(teamName: String, teamId: Int)
        fun onClickEvents(team_id: Long, team_name: String, isLeader: Boolean)
    }
}