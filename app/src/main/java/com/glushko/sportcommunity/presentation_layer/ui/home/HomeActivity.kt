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
            modelNotification.deleteNotificationChats()
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

    private fun openProfileFragment(user_id: Int?, user_name: String?, isMe: Boolean = true){
        if(user_id!=null && user_name!=null){
            val fragmentProfile =  ProfileFragment(userId = user_id, userName = user_name, isMe = isMe, callbackActivity = object : ProfileFragment.Callback{
                override fun onClickTeam(teamName: String, teamDesc: String, bitmap: Bitmap, leader_id: Int) {
                    openTeamFragment(teamName, teamDesc, bitmap, leader_id)
                }

                override fun onClickBtnLeft(isMe: Boolean) {
                    if(isMe){
                        openNotificationFragment()
                    }else{
                        openAddDialogFragment()
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
            override fun changeFragment(contact_id: Long, contact_name: String) {
                toolbar.title = contact_name
                openDialogFragment(contact_id)
            }

        })
        supportFragmentManager.beginTransaction().replace(fragmentContainer, frgmentChats).commit()
    }

    private fun openDialogFragment(id_user: Long) {
        supportFragmentManager.beginTransaction().add(fragmentContainer,DialogFragment(id_user)).commit()
    }

    private fun openSettingFragment() {
        toolbar.title = btnSettingText.text
        supportFragmentManager.beginTransaction().replace(fragmentContainer, SettingFragment()).commit()
    }

    private fun openAddDialogFragment() {
        Toast.makeText(this, "Диалог фрагмент отправки запроса на дружбу", Toast.LENGTH_SHORT).show()
    }

    private fun openNotificationFragment() {
        toolbar.title = btnNotificationText.text
        supportFragmentManager.beginTransaction().replace(fragmentContainer, NotificationFragment()).commit()
    }

    private fun openTeamFragment(teamName: String, teamDesc: String, bitmap: Bitmap, leader_id: Int){
        toolbar.title = teamName
        val userId = dataLogin.value?.idUser?:0
        var isLeader = false
        if(userId == leader_id)
            isLeader = true
        supportFragmentManager.beginTransaction().add(fragmentContainer, TeamFragment(teamName, teamDesc, bitmap, isLeader)).commit()
    }

    private fun openFriendsFragment(){
        val fragmentFriends = FriendsFragment(object : FriendsFragment.Callback{
            override fun changeFragment(friend_id: Int, friend_name: String) {
                openProfileFragment(friend_id, friend_name, false)
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

