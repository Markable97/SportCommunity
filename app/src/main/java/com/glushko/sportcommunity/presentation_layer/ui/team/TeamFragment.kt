package com.glushko.sportcommunity.presentation_layer.ui.team

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import com.glushko.sportcommunity.presentation_layer.ui.notification.NotificationFragment
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel
import kotlinx.android.synthetic.main.activity_team_profile.*

class TeamFragment() : Fragment() {

    private var leaderName: String? = null
    private var leaderId: Long? = null
    private var isLeader: Boolean? = null
    private var teamId: Int? = null
    private lateinit var callback: CallbackTeamFragment
    private var user_id: Long? = null
    constructor(teamName: String, teamDescription: String, bitmap: Bitmap, leaderId: Long, leaderName: String,
                isLeader: Boolean, teamId: Int) : this() {
        this.leaderName = leaderName
        this.leaderId = leaderId
        this.teamName = teamName
        this.teamDescription = teamDescription
        this.bitmap = bitmap
        this.isLeader = isLeader
        this.teamId = teamId

    }

    private var typeOpen: String? = null

    val layoutId: Int = R.layout.activity_team_profile
    private var bitmap: Bitmap? = null
    var teamName: String? = null
    private var teamDescription: String? = null
    private var inTeam: Int = 0
    lateinit var modelSquad: SquadViewModel

    companion object{
        const val TAG = "KEY_TEAM"
        const val TYPE_OPEN = "type_open"
        const val KEY1 = "team_id"
        const val KEY2 = "team_name"
        const val KEY3 = "user_id"
        const val KEY4 = "team_desc"
        const val KEY5 = "leader_name"
        const val KEY6 = "leader_id"
        const val KEY7 = "bitmap"

        fun newInstance(type_open: String, team_id: Long, team_name: String, user_id: Long, team_desc: String, leader_name: String, leader_id: Long, bitmap: Bitmap): TeamFragment{
            return TeamFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY1, team_id)
                    putString(KEY2, team_name)
                    putLong(KEY3, user_id)
                    putString(KEY4, team_desc)
                    putString(KEY5, leader_name)
                    putLong(KEY6, leader_id)
                    putString(TYPE_OPEN, type_open)
                    putParcelable(KEY7, bitmap)

                }
            }
        }
        fun newInstance(type_open: String, team_id: Long, team_name: String, user_id: Long): TeamFragment{
            return TeamFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY1, team_id)
                    putString(KEY2, team_name)
                    putLong(KEY3, user_id)
                    putString(TYPE_OPEN, type_open)

                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            callback = context as CallbackTeamFragment
        }catch(ex: Exception){
            println("Bad callback fragment")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelSquad = ViewModelProviders.of(this).get(SquadViewModel::class.java)

        modelSquad.liveDataTeamInfo.observe(this, Observer {
            println("TeamFragment live data 1 ${it.message}")
            if(it.success == 1){
                val teamInfo = it.teamsUserinfo.first()
                isLeader = user_id?:0 == teamInfo.leader_id.toLong()
                this.leaderId = teamInfo.leader_id.toLong()
                this.leaderName = teamInfo.leader_name
                if(isLeader!!){
                    team_profile_btn_2.text = getString(R.string.team_profile_btn_2)
                }else{
                    team_profile_btn_2.text = getString(R.string.team_profile_btn_2_dop)
                }
                teamDescription = teamInfo.team_desc
                team_description.text = teamDescription
            }else{
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            this.typeOpen = it.getString(TYPE_OPEN)
            if(this.typeOpen == "from notification") {
                this.inTeam = 0
                this.teamId = it.getLong(KEY1).toInt()
                this.teamName = it.getString(KEY2)
                this.user_id = it.getLong(KEY3)
            }else{
                this.inTeam = 1
                this.teamId = it.getLong(KEY1).toInt()
                this.teamName = it.getString(KEY2)
                this.user_id = it.getLong(KEY3)
                this.teamDescription = it.getString(KEY4)
                this.leaderName = it.getString(KEY5)
                this.leaderId = it.getLong(KEY6)
                this.bitmap = it.getParcelable(KEY7)
                isLeader = leaderId == user_id
            }
        }
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(typeOpen != null){
            if(typeOpen == "from notification"){
                if(teamId != null) {
                    modelSquad.getTeamInfo(teamId!!.toLong())
                }
            }
        }
        /*typeOpen?.let {
            if(it == "from notification"){
                teamId?.let {
                    modelSquad.getTeamInfo(it.toLong())
                }
            }
        }*/
        if(this.bitmap != null){
            team_image_profile.setImageBitmap(bitmap)
        }else{
            Glide.with(requireContext())
                .load(NetworkService.BASE_URL_IMAGE+this.teamName+".png")
                .placeholder(R.drawable.chatplaceholder)
                .into(team_image_profile)
        }
        team_name.text = teamName?:""
        team_description.text = teamDescription?:""
        isLeader?.let {
            if(!it){
                team_profile_btn_2.text = getString(R.string.team_profile_btn_2_dop)
            }
        }


        team_profile_btn_2.setOnClickListener {
            isLeader?.let {
                if(!it){
                    if(leaderId != null && leaderName != null){
                        callback.onClickUpperRightButton(leaderId!!, leaderName!!)
                    }

                }
            }
        }

        team_profile_btn_chat.setOnClickListener {
            val teamId = teamId?:0
            if(inTeam == 1){
                callback.onClickUpperLeftButton(teamName!!, teamId)
            }else{
                Toast.makeText(requireContext(), "Вы не в команде. Чат недоступен", Toast.LENGTH_SHORT).show()
            }
        }

        btn_team_squad.setOnClickListener {
            val teamId = teamId?:0
            callback.onClickSquad(teamId.toLong(),teamName!!, isLeader!!)
        }

        btn_squad_events.setOnClickListener {
            val teamId = teamId?:0
            if(inTeam == 1){
                callback.onClickEvents(teamId.toLong(), teamName!!, isLeader!!)
            }else{
                Toast.makeText(requireContext(), "Вы не в команде. События недоступны", Toast.LENGTH_SHORT).show()
            }
        }

    }

    interface CallbackTeamFragment{
        fun onClickUpperRightButton(idLeader: Long, leaderName: String)
        fun onClickSquad(team_id: Long, team_name: String, isLeader: Boolean)
        fun onClickUpperLeftButton(teamName: String, teamId: Int)
        fun onClickEvents(team_id: Long, team_name: String, isLeader: Boolean)
    }
}