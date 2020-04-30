package com.glushko.sportcommunity.presentation_layer.ui.home

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.glushko.sportcommunity.R

import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.navigation.*


class HomeActivity : AppCompatActivity() {

    open val contentId = R.layout.home_activity

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

        btnFriends.setOnClickListener {
            Toast.makeText(this, "This friands", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawers()
            toolbar.title = btnFriendsText.text
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ProfileFragment()).commit()
        }


    }


}

