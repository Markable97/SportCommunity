package com.glushko.sportcommunity.presentation_layer.ui.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.vm.AccountViewModel
import kotlinx.android.synthetic.main.register_activity.*
import kotlinx.coroutines.*

class RegisterFragment : BaseFragment() {
    override val layoutId = R.layout.register_activity
    override val titleToolbar = R.string.register

    lateinit var model: AccountViewModel

    var downloding: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        val data: LiveData<String> = model.getData()
        data.observe(this, Observer<String>{it: String ->
            super.showMessage("$it")
            super.hideProgress()
            downloding = false
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
            super.showMessage("Данный функционал не поддерживается")
        }
    }

    private fun register() {
        super.hideSoftKeyboard()

        val allValid = validateFields()

        if (allValid) {
            showProgress()
            model.registerUser(etEmail.toString(), etUsername.toString(), etPassword.toString())
            /*accountViewModel.register(
                etEmail.text.toString(),
                etusername.text.toString(),
                etPassword.text.toString()
            )*/
        }
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
        val test = etPassword.toString().length
        val test2 = etConfirmPassword.toString().isEmpty()
        if(etPassword.toString().isEmpty() || etConfirmPassword.toString().isEmpty()){
            return false
        }
        val valid = etPassword.text.toString() == etConfirmPassword.text.toString()
        if (!valid) {
            showMessage(getString(R.string.error_password_mismatch))
        }

        return valid

    }

}