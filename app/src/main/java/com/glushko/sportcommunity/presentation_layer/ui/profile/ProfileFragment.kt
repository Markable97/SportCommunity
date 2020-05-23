package com.glushko.sportcommunity.presentation_layer.ui.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.TeamsUserInfo
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMainPage
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.vm.ProfileViewModel
import com.realpacific.clickshrinkeffect.applyClickShrink
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment(val userId: Int = 0, val userName: String = "" ,val isMe: Boolean = true,val callbackActivity: Callback ) : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_profile
    override val titleToolbar: Int = R.string.screen_profile




    lateinit var adapter: ProfileTeamsAdapter
    lateinit var modelPage: ProfileViewModel
    lateinit var dataProfile: MutableLiveData<ResponseMainPage>

    var listInfoProfile: MutableList<TeamsUserInfo.Params> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelPage = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        dataProfile = modelPage.getData(userId.toLong())


        dataProfile.observe(this, Observer<ResponseMainPage>{
            println("Live data 2")
            println("ProfileFragment: \n${it.success} ${it.message}, ${it.teamsUserinfo}")
            if(it.success == 1){
                listInfoProfile = it.teamsUserinfo
                adapter.setList(listInfoProfile)
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_profile_1.applyClickShrink()
        bt_profile_2.applyClickShrink()

        if(isMe){
            btn_profile_1.background = context?.getDrawable(R.drawable.ic_notifications_black_36dp)
            bt_profile_2.background = context?.getDrawable(R.drawable.ic_settings_black_36dp)
        }else{
            btn_profile_1.background = context?.getDrawable(R.drawable.ic_check)
            bt_profile_2.background = context?.getDrawable(R.drawable.ic_chat)
        }

        adapter = ProfileTeamsAdapter(listInfoProfile, object : ProfileTeamsAdapter.Callback{
            override fun onItemClicked(item: TeamsUserInfo.Params, bitmap: Bitmap) {
                Toast.makeText(activity, "This is team - ${item.team_name}", Toast.LENGTH_SHORT).show()
                callbackActivity.onClickTeam(item.team_name, item.team_desc, bitmap, item.leader_id)
            }

        })
        profile_recycler_teams.adapter = adapter
        profile_recycler_teams.layoutManager = LinearLayoutManager(activity)

        btn_profile_1.setOnClickListener {
            callbackActivity.onClickBtnLeft(isMe)
        }
        bt_profile_2.setOnClickListener {
            callbackActivity.onClickBtnRight(isMe, userId, userName)
        }
    }

    override fun onResume() {
        super.onResume()
        println("ProfileFragment: OnResume")
        tv_profile_name.text = userName
    }

    interface Callback{
        fun onClickTeam(teamName: String, teamDesc: String, bitmap: Bitmap, leader_id: Int)

        fun onClickBtnLeft(isMe: Boolean)

        fun onClickBtnRight(isMe: Boolean, idUser: Int = 0, userName: String = "")
    }

    override fun onPause() {
        super.onPause()
        println("ProfileFragment: OnResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("ProfileFragment: OnDestroy")
    }
}
