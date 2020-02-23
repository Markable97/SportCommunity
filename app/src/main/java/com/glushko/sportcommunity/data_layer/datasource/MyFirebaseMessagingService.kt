package com.glushko.sportcommunity.data_layer.datasource

import android.content.Context
import android.util.Log
import com.glushko.sportcommunity.data_layer.datasource.ApiService.Companion.PARAM_EMAIL
import com.glushko.sportcommunity.data_layer.datasource.ApiService.Companion.PARAM_TOKEN
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager.Companion.ACCOUNT_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, remoteMessage.notification?.body)

    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "Refreshed token: $token")
        //println("Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       // sendRegistrationToServer(token)
        var oldToken = getSharedPreferences(this::javaClass.name, Context.MODE_PRIVATE).getString(ACCOUNT_TOKEN, "")
        if( oldToken != "") {
            if(oldToken!=token){
                getSharedPreferences(this::javaClass.name, Context.MODE_PRIVATE).edit().putString(ACCOUNT_TOKEN, "").apply()
                //update на севрере
            }else{
                println("токен не обновился")
            }

        }else{
            getSharedPreferences(this::javaClass.name, Context.MODE_PRIVATE).edit().putString(ACCOUNT_TOKEN, "").apply()
        }
    }

}