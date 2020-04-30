package com.glushko.sportcommunity.presentation_layer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.home.HomeActivity
import com.glushko.sportcommunity.presentation_layer.ui.login.LoginActivity
import com.glushko.sportcommunity.presentation_layer.ui.register.RegisterActivity

class Navigator {

    fun showMain(context: Context){
        //провекра на вход: Есть ли данные в SharedPreferences, есть ли связь + подходит ли логин и пароль
        //showHome(context, false)
        val account = SharedPrefsManager(context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)).getAccount()
        if(account.password.isNotEmpty()){
            showHome(context, false)
        }else{
            showLogin(context, false)
        }
    }


    fun showHome(context: Context, newTask: Boolean = true) = context.startActivity<HomeActivity>(newTask = newTask)

    fun showLogin(context: Context, newTask: Boolean = true) = context.startActivity<LoginActivity>(newTask = newTask)

    fun showSignUp(context: Context) = context.startActivity<RegisterActivity>()
}

private inline fun<reified T> Context.startActivity(newTask: Boolean = false, args: Bundle? = null){
    /**
     * apply Полезна в тех случаях, когда требуется создание экземпляра
     * у которого следует инициализировать некоторые свойства. Часто в этих случаях мы просто повторяем имя экземпляра.
     * */
    val newScreen = Intent(this, T::class.java).apply{
        if(newTask) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        putExtra("args", args)
    }
    this.startActivity(newScreen)

}