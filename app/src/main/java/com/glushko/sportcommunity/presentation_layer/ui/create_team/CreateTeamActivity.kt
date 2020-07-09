package com.glushko.sportcommunity.presentation_layer.ui.create_team

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.glushko.sportcommunity.R

import kotlinx.android.synthetic.main.activity_create_team.*
import kotlinx.android.synthetic.main.toolbar.*

class CreateTeamActivity : AppCompatActivity() {

    val contentId = R.layout.activity_create_team
    lateinit var nav_host_fragment: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentId)
        setSupportActionBar(toolbar)

        nav_host_fragment = Navigation.findNavController(this, R.id.nav_host_fragment)

    }

}
