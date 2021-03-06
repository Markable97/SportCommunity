package com.glushko.sportcommunity.data_layer.repository

import android.content.SharedPreferences
import com.glushko.sportcommunity.business_logic_layer.domain.Register

class SharedPrefsManager(private val prefs: SharedPreferences) {
    companion object {
        const val ACCOUNT_TOKEN = "account_token"
        const val ACCOUNT_ID = "account_id"
        const val ACCOUNT_NAME = "account_name"
        const val ACCOUNT_EMAIL = "account_email"
        const val ACCOUNT_STATUS = "account_status"
        const val ACCOUNT_DATE = "account_date"
        const val ACCOUNT_IMAGE = "account_image"
        const val ACCOUNT_PASSWORD = "account_password"

        private var INSTANCE: SharedPrefsManager? = null

        fun getSharedPrefsManager(prefs: SharedPreferences): SharedPrefsManager{
            if(INSTANCE==null) {
                INSTANCE = SharedPrefsManager(prefs)

            }
            return INSTANCE!!
        }


    }

    fun saveAccount(account: Register.Params){
        prefs.edit().apply {
            //putSafely(ACCOUNT_ID, account.id)
            putSafely(ACCOUNT_NAME, account.name)
            putSafely(ACCOUNT_EMAIL, account.email)
            putSafely(ACCOUNT_TOKEN, account.token)
            //putString(ACCOUNT_STATUS, account.status)
            //putSafely(ACCOUNT_DATE, account.userDate)
            //putSafely(ACCOUNT_IMAGE, account.image)
            putSafely(ACCOUNT_PASSWORD, account.password)
            putSafely(ACCOUNT_ID, account.idUser)
        }.apply()
    }

    fun updateToken(token: String){
        prefs.edit().apply{
            putSafely(ACCOUNT_TOKEN, token)
        }.apply()
    }

    fun logout(){
        prefs.edit().apply{
            putInt(ACCOUNT_ID, 0)
            putString(ACCOUNT_NAME, "")
            putString(ACCOUNT_PASSWORD, "")
            putString(ACCOUNT_EMAIL, "")
            putString(ACCOUNT_TOKEN, "")
        }.apply()
    }

    fun getToken(): String{
        return prefs.getString(ACCOUNT_TOKEN, "")!!
    }

    fun getAccount():Register.Params {
        //val id = prefs.getLong(ACCOUNT_ID, 0)


        val account = Register.Params(
            prefs.getString(ACCOUNT_EMAIL, "")!!,
            prefs.getString(ACCOUNT_NAME, "")!!,
            prefs.getString(ACCOUNT_PASSWORD, "")!!,
            prefs.getString(ACCOUNT_TOKEN, "")!!,
            prefs.getInt(ACCOUNT_ID, 0)
        )

        return account
    }
}

fun SharedPreferences.Editor.putSafely(key: String, value: Int?) {
    if(value!= null && value!=0){
        putInt(key, value)
    }
}

fun SharedPreferences.Editor.putSafely(key: String, value: String?) {
    if (value != null && value.isNotEmpty()) {
        putString(key, value)
    }
}