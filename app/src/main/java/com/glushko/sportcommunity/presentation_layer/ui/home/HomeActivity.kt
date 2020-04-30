package com.glushko.sportcommunity.presentation_layer.ui.home

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.glushko.sportcommunity.R

import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.chat.ChatsFragment
import com.glushko.sportcommunity.presentation_layer.ui.notification.NotificationFragment
import com.glushko.sportcommunity.presentation_layer.ui.profile.ProfileFragment
import com.glushko.sportcommunity.presentation_layer.ui.setting.SettingFragment
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.navigation.*


class HomeActivity : AppCompatActivity() {

    open val contentId = R.layout.home_activity
    val fragmentContainer = R.id.fragmentContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentId)
        setSupportActionBar(toolbar)

        val account = SharedPrefsManager(this.getSharedPreferences(this.packageName, Context.MODE_PRIVATE)).getAccount()

        println("Home activity $account \n")

        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        btnChats.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = btnChatsText.text
            supportFragmentManager.beginTransaction().replace(fragmentContainer, ChatsFragment()).commit()
        }

        btnFriends.setOnClickListener {
            Toast.makeText(this, "This friends", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawers()
            toolbar.title = btnFriendsText.text
            supportFragmentManager.beginTransaction().replace(fragmentContainer, ProfileFragment()).commit()
        }
        btnNotification.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = btnNotificationText.text
            supportFragmentManager.beginTransaction().replace(fragmentContainer, NotificationFragment()).commit()
        }

        btnSetting.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = btnSettingText.text
            supportFragmentManager.beginTransaction().replace(fragmentContainer, SettingFragment()).commit()
        }

    }


}

