package com.glushko.sportcommunity.presentation_layer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RouteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navigator = Navigator().showMain(this)
    }
}