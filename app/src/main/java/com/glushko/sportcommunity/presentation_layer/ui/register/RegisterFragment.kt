package com.glushko.sportcommunity.presentation_layer.ui.register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.vm.RegisterViewModel
import kotlinx.android.synthetic.main.register_activity.*
import kotlinx.coroutines.*

class RegisterFragment : BaseFragment() {
    override val layoutId = R.layout.register_activity
    override val titleToolbar = R.string.register

    lateinit var model: RegisterViewModel

    var downloding: Boolean = false
    lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         model = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        val data: LiveData<String> = model.getData()
        data.observe(this, Observer<String>{it: String ->
            super.showMessage("$it")
            etUsername.setText("Тестовое значение из LiveData")
            super.hideProgress()
            downloding = false
        })
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        btnNewMembership.setOnClickListener {

            register()
            /*if(!downloding){
                downloding = true
                super.showMessage("Start loading")
                super.showProgress()
                job = GlobalScope.launch(Dispatchers.IO) {
                    model.registerUser()
                }
            }else{
                println("Already downloading")
            }*/


        }

        btnAlreadyHaveAccount.setOnClickListener {

            if(downloding){
                if (job.isActive){
                    job.cancel()
                    super.hideProgress()
                    super.showMessage("End loading")
                    downloding = false
                }
            }else{
                println("Enough downloading")
            }


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
        val allValid = true
        for(field in allFields){
            field.testValidity() && allValid
        }
        return allValid && validatePasswords()
    }
    private fun validatePasswords(): Boolean {
        val valid = etPassword.text.toString() == etConfirmPassword.text.toString()
        if (!valid) {
            showMessage(getString(R.string.error_password_mismatch))
        }
        return valid
    }

}