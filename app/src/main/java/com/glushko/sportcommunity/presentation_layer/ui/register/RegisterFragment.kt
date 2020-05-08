package com.glushko.sportcommunity.presentation_layer.ui.register

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.data_layer.datasource.ApiService.Companion.PARAM_TOKEN
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.vm.AccountViewModel
import kotlinx.android.synthetic.main.register_activity.*
import kotlinx.coroutines.*

class RegisterFragment : BaseFragment() {
    override val layoutId = R.layout.register_activity
    override val titleToolbar = R.string.register
    val context = activity
    lateinit var model: AccountViewModel
    //lateinit var registerUser: Register.Params
    private var downloding: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        val data: LiveData<String> = model.getData()
        data.observe(this, Observer<String>{it: String ->
            super.showMessage("$it")
            super.hideProgress()
            downloding = false
            if(it == "success"){
                model.saveAccountRepository()
                activity?.finish()
            }

        })
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnNewMembership.setOnClickListener {
            if(!downloding){
                downloding = true
                super.showProgress()
                register()
            }else{
                println("Already downloading")
            }
        }

        btnAlreadyHaveAccount.setOnClickListener {
            activity?.finish()
        }
    }

    private fun register() {
        super.hideSoftKeyboard()

        val allValid = validateFields()

        if (allValid) {
            showProgress()

            model.registerUser(etEmail.text.toString(), etUsername.text.toString(), etPassword.text.toString(), validToken()!!)
        }
    }

    private fun validToken(): String? {
        val token = SharedPrefsManager(activity!!.getSharedPreferences(activity!!.packageName, MODE_PRIVATE)).getToken()
        println("token in RegisterFragment $token")
        return token

    }

    private fun validateFields(): Boolean {
        val allFields = arrayOf(etEmail, etPassword, etConfirmPassword, etUsername)
        var allValid = true
        for(field in allFields){
            field.testValidity() && allValid
        }
        return allValid && validatePasswords()
    }
    private fun validatePasswords(): Boolean {
        if(etPassword.text.toString().isEmpty() || etConfirmPassword.text.toString().isEmpty()){
            return false
        }
        val valid = etPassword.text.toString() == etConfirmPassword.text.toString()
        if (!valid) {
            showMessage(getString(R.string.error_password_mismatch))
        }

        return valid

    }

}