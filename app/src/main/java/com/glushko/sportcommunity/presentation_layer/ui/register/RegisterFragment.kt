package com.glushko.sportcommunity.presentation_layer.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.register_activity.*

class RegisterFragment : BaseFragment() {
    override val layoutId = R.layout.register_activity
    override val titleToolbar = R.string.register


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        btnNewMembership.setOnClickListener {
            super.showMessage("Start loading")
            super.showProgress()
        }

        btnAlreadyHaveAccount.setOnClickListener {
            super.hideProgress()
            super.showMessage("End loading")
        }
    }
}