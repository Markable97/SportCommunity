package com.glushko.sportcommunity.presentation_layer.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
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
import java.io.File

class ProfileFragment(val userId: Int = 0, val userName: String = "" ,val isMe: Boolean = true, private var status_friend: String? ,val callbackActivity: Callback ) : BaseFragment() {

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

        modelPage.liveDataFriendShip.observe(this, Observer {
            println("Live Data 1")
            println("ProfileFragment: FriendShip action ${it.success} ${it.message}")
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            status_friend = when(it.message){
                "request friend complete" -> "request"
                "request reject" -> null
                "delete friend" -> null
                "confirm friend" -> "friend"
                else -> null
            }
            iconFriendship(status_friend)

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
            iconFriendship(status_friend)

            bt_profile_2.background = context?.getDrawable(R.drawable.ic_chat)
        }

        adapter = ProfileTeamsAdapter(listInfoProfile, object : ProfileTeamsAdapter.Callback{
            override fun onItemClicked(item: TeamsUserInfo.Params, bitmap: Bitmap) {
                Toast.makeText(activity, "This is team - ${item.team_name}", Toast.LENGTH_SHORT).show()
                callbackActivity.onClickTeam(item.team_name, item.team_desc, bitmap, item.leader_id, item.leader_name)
            }

        })
        profile_recycler_teams.adapter = adapter
        profile_recycler_teams.layoutManager = LinearLayoutManager(activity)

        btn_profile_1.setOnClickListener {
            if(isMe){
                callbackActivity.onClickBtnLeft(isMe, status_friend)
            }else{
                modelPage.friendshipAction(friend_id = userId.toLong(),
                    action = when(status_friend){
                        "friend" -> "delete"
                        "request" -> "reject_request"
                        "head_request" ->  "accept_request"
                        else -> "add"
                    }
                    )
            }


        }
        bt_profile_2.setOnClickListener {
            callbackActivity.onClickBtnRight(isMe, userId, userName)
        }

        ivUserImage.setOnClickListener {
            photoFile = createImageFile()
            photoUri = createUri(photoFile)
            super.takePhotoIntent(photoUri)
            //super.takePhotoIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        println("ProfileFragment: OnResume")
        tv_profile_name.text = userName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==TAKE_PICTURE_REQUEST && resultCode == RESULT_OK){
            val file = File(currentPhotoPath)
            val photo: Uri = Uri.fromFile(file)
            ivUserImage.setImageURI(photo)
            super.saveImage(((ivUserImage.drawable) as BitmapDrawable).bitmap, "${System.currentTimeMillis()}")
        }
    }

    private fun iconFriendship(status_friend: String?){
        when(status_friend){
            "friend" -> btn_profile_1.background = context?.getDrawable(R.drawable.ic_check)
            "request" -> btn_profile_1.background = context?.getDrawable(R.drawable.ic_group_add_black_24dp)
            else -> btn_profile_1.background = context?.getDrawable(R.drawable.ic_group_add_black_24dp)
        }
    }

    interface Callback{
        fun onClickTeam(teamName: String, teamDesc: String, bitmap: Bitmap, leader_id: Int, leader_name: String)

        fun onClickBtnLeft(isMe: Boolean, status_friend: String?)

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
