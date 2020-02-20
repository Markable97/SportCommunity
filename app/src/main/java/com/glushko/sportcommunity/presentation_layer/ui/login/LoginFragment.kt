package com.glushko.sportcommunity.presentation_layer.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.vm.AccountViewModel
import kotlinx.android.synthetic.main.login_activity.*

class LoginFragment: BaseFragment(){
    override val layoutId: Int = R.layout.login_activity
    override val titleToolbar: Int = R.string.screen_login

    lateinit var model: AccountViewModel
    lateinit var data: LiveData<String>
    lateinit var dataLogin: LiveData<Register.Params>

    var downloding: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        data = model.getData()
        data.observe(this, Observer<String>{
            if(it == "success"){

            }
            super.showMessage("$it")
            super.hideProgress()
            downloding = false
        })


    }

    override fun onResume() {
        super.onResume()
        dataLogin  = model.getLoginData()
        dataLogin.observe(this, Observer<Register.Params>{account: Register.Params ->
            if(account.email.isNotEmpty()){
                etEmail.setText(account.email)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            if(!downloding){
                downloding = true
                login()
            }else{
                println("Already downloading")
            }
        }
        btnRegister.setOnClickListener{
            activity?.let { navigator.showSignUp(it) }
        }

        btnForgetPassword.setOnClickListener {
            super.showMessage("Нет функционала")
        }
    }

    private fun login(){
        val email = etEmail.testValidity()
        val password = etPassword.testValidity()
        super.hideSoftKeyboard()
        //super.showMessage("$email $password")
        if(email && password){
            super.showProgress()
            model.loginUser(etEmail.text.toString(), etPassword.text.toString())
        }else{
            downloding = false
        }
    }
}