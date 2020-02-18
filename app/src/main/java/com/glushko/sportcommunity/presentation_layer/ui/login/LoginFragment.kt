package com.glushko.sportcommunity.presentation_layer.ui.login

import android.os.Bundle
import android.view.View
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import kotlinx.android.synthetic.main.login_activity.*

class LoginFragment: BaseFragment(){
    override val layoutId: Int = R.layout.login_activity
    override val titleToolbar: Int = R.string.screen_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            super.showMessage("Нет функционала")
        }
        btnRegister.setOnClickListener{
            activity?.let { navigator.showSignUp(it) }
        }
    }
}