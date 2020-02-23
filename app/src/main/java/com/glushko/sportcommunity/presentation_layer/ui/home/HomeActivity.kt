package com.glushko.sportcommunity.presentation_layer.ui.home

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : AppCompatActivity() {

    open val contentId = R.layout.home_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentId)
        setSupportActionBar(toolbar)

        val account = SharedPrefsManager(this.getSharedPreferences(this.packageName, Context.MODE_PRIVATE)).getAccount()

        val token = SharedPrefsManager(this.getSharedPreferences(this.packageName, Context.MODE_PRIVATE)).getToken()

        println("Home activity $account \n and token = $token")
        test_text_view.text = "$account"


    }
}

