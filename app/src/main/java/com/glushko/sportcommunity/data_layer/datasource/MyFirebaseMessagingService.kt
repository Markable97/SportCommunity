package com.glushko.sportcommunity.data_layer.datasource

import android.content.Context
import android.util.Log
import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseNotificationHelper
import com.glushko.sportcommunity.data_layer.repository.ChatsNotification
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager.Companion.ACCOUNT_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println(TAG + " From: ${remoteMessage.from}")
        //val dao = MainDatabase.getMessageDao(this)
        //val notificationDao = MainDatabase.getNotificationDao(this)
        remoteMessage.data.isNotEmpty().let {
            println(TAG + " Message data payload: " + remoteMessage.data)
            val messageTypeApp = remoteMessage.data["type_notification_app"]?:""
            val notificationHelper = UseCaseNotificationHelper(this, remoteMessage)
            when(messageTypeApp){
                "message" -> notificationHelper.addMessageInDatabase()
                "friendship" -> notificationHelper.addFriendshipInDatabase()
                "invitation in team" -> notificationHelper.addNotification()
                "event" ->  notificationHelper.addNotification()
                else -> println("Необработанное уведомление")
            }

        }


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