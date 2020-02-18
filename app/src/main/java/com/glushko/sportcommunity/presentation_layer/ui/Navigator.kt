package com.glushko.sportcommunity.presentation_layer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.glushko.sportcommunity.presentation_layer.ui.login.LoginActivity
import com.glushko.sportcommunity.presentation_layer.ui.register.RegisterActivity

class Navigator {

    fun showMain(context: Context){
        showLogin(context, false)
    }

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