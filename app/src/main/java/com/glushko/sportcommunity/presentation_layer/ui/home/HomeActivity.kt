package com.glushko.sportcommunity.presentation_layer.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.data_layer.repository.ChatsNotification
import com.glushko.sportcommunity.data_layer.repository.MainDatabase


import com.glushko.sportcommunity.presentation_layer.ui.BaseActivity
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.ui.Navigator
import com.glushko.sportcommunity.presentation_layer.ui.chat.ChatsFragment
import com.glushko.sportcommunity.presentation_layer.ui.dialog.DialogFragment
import com.glushko.sportcommunity.presentation_layer.ui.friends.FriendsFragment
import com.glushko.sportcommunity.presentation_layer.ui.notification.NotificationFragment
import com.glushko.sportcommunity.presentation_layer.ui.profile.ProfileFragment
import com.glushko.sportcommunity.presentation_layer.ui.setting.SettingFragment
import com.glushko.sportcommunity.presentation_layer.ui.team.TeamFragment
import com.glushko.sportcommunity.presentation_layer.vm.AccountViewModel
import com.glushko.sportcommunity.presentation_layer.vm.ChatsViewModel
import com.glushko.sportcommunity.presentation_layer.vm.NotificationDrawerViewModel
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.navigation.*
import kotlinx.android.synthetic.main.toolbar.*


class HomeActivity :  AppCompatActivity() {

    val contentId = R.layout.home_activity
    val fragmentContainer = R.id.fragmentContainer

    val navigator = Navigator()

    companion object{
        open var USER_ID: Long = 0
    }


    lateinit var model: AccountViewModel
    lateinit var dataLogin: LiveData<Register.Params>
    lateinit var modelNotification: NotificationDrawerViewModel


    //lateinit var dataNotificationChats: LiveData<List<ChatsNotification>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentId)
        setSupportActionBar(toolbar)

        setSupportActionBar(toolbar)
        toolbar.title = "Профиль"


        model = ViewModelProviders.of(this).get(AccountViewModel::class.java)

        dataLogin = model.getLoginData()
        dataLogin.observe(this, Observer<Register.Params> {
            tvUserName.text = it.name
            tvUserEmail.text = it.email
            USER_ID = it.idUser.toLong()
            openProfileFragment(it.idUser, it.name)
        })



        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
                if(newState == DrawerLayout.STATE_SETTLING){
                    hideSoftKeyboard()
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        modelNotification = ViewModelProviders.of(this).get(NotificationDrawerViewModel::class.java)

        modelNotification.chatsLiveData.observe(this, Observer {
            println("HomeActivity Live Data Notification 1 ${it}")
            val count = if(it.isNotEmpty()) it.first().count else 0
            if(count > 0 ){
                btnChats_notification.text = if(count > 9) "9+" else count.toString()
                btnChats_notification.visibility = View.VISIBLE
            }else{
                btnChats_notification.visibility = View.GONE
            }
        })


        profileContainer.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = "Профиль"
            openProfileFragment(dataLogin.value?.idUser, dataLogin.value?.name)

        }

        btnChats.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = btnChatsText.text
            openChatsFragment()
        }


        btnFriends.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = btnFriendsText.text
            openFriendsFragment()

        }
        btnNotification.setOnClickListener {
            drawerLayout.closeDrawers()
            openNotificationFragment()
        }

        btnSetting.setOnClickListener {
            drawerLayout.closeDrawers()
            openSettingFragment()
        }

        btnLogout.setOnClickListener {
            model.logout()
            navigator.showLogin(this)
        }
    }

    private fun openProfileFragment(user_id: Int?, user_name: String?, status_friend: String? = null, isMe: Boolean = true){
        if(user_id!=null && user_name!=null){
            val fragmentProfile =  ProfileFragment(userId = user_id, userName = user_name, isMe = isMe, status_friend = status_friend, callbackActivity = object : ProfileFragment.Callback{
                override fun onClickTeam(teamName: String, teamDesc: String, bitmap: Bitmap, leader_id: Int, leader_name: String) {
                    openTeamFragment(teamName, teamDesc, bitmap, leader_id, leader_name)
                }

                override fun onClickBtnLeft(isMe: Boolean, status_friend: String?) {
                    if(isMe){
                        openNotificationFragment()
                    }else{
                        openAddDialogFragment(status_friend)
                    }
                }

                override fun onClickBtnRight(isMe: Boolean, idUser: Int, user_name: String) {
                    if(isMe){
                        openSettingFragment()
                    }else{
                        toolbar.title = user_name
                        openDialogFragment(idUser.toLong())
                    }
                }

            })
            supportFragmentManager.beginTransaction().replace(fragmentContainer, fragmentProfile).commit()
        }
    }

    private fun openChatsFragment(){
        val frgmentChats = ChatsFragment(object : ChatsFragment.Callback{
            override fun changeFragment(contact_id: Long, contact_name: String, count_notification: Int) {
                toolbar.title = contact_name
                modelNotification.deleteChooseNotificationChat(contact_id)
                openDialogFragment(contact_id, count_notification)
            }

        })
        supportFragmentManager.beginTransaction().replace(fragmentContainer, frgmentChats).commit()
    }

    private fun openDialogFragment(id_user: Long, count_notification: Int = 0) {
        supportFragmentManager.beginTransaction().add(fragmentContainer,DialogFragment(id_user, count_notification)).commit()
    }

    private fun openSettingFragment() {
        toolbar.title = btnSettingText.text
        supportFragmentManager.beginTransaction().replace(fragmentContainer, SettingFragment()).commit()
    }

    private fun openAddDialogFragment(status_friend: String?) {
        //Toast.makeText(this, "Диалог фрагмент отправки запроса на дружбу", Toast.LENGTH_SHORT).show()
        when(status_friend){
            "friend" -> Toast.makeText(this, "Это ваш друг.", Toast.LENGTH_SHORT).show()
            "request" -> Toast.makeText(this, "Запрос на дружбу уже отправлен", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "Запрос на дружбу", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openNotificationFragment() {
        toolbar.title = btnNotificationText.text
        supportFragmentManager.beginTransaction().replace(fragmentContainer, NotificationFragment()).commit()
    }

    private fun openTeamFragment(teamName: String, teamDesc: String, bitmap: Bitmap, leader_id: Int, leader_name: String){
        toolbar.title = teamName
        val userId = dataLogin.value?.idUser?:0
        var isLeader = false
        if(userId == leader_id)
            isLeader = true
        val fragmentTeamProfile = TeamFragment(teamName, teamDesc, bitmap, leader_id.toLong(), leader_name, isLeader, object : TeamFragment.Callback{
            override fun onClickUpperRightButton(idLeeader: Long, leaderName: String) {
                toolbar.title = leaderName
                openDialogFragment(idLeeader)
            }

        })
        supportFragmentManager.beginTransaction().add(fragmentContainer, fragmentTeamProfile).commit()
    }

    private fun openFriendsFragment(){
        val fragmentFriends = FriendsFragment(object : FriendsFragment.Callback{
            override fun changeFragment(friend_id: Int, friend_name: String, status_friend: String?) {
                openProfileFragment(friend_id, friend_name, status_friend,false)
            }

        })
        supportFragmentManager.beginTransaction().replace(fragmentContainer, fragmentFriends).commit()
    }


    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}

