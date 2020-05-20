package com.glushko.sportcommunity.data_layer.datasource

import android.content.Context
import android.util.Log
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager.Companion.ACCOUNT_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println(TAG + " From: ${remoteMessage.from}")
        val dao = MainDatabase.getDatabase(this).messageDao()
        remoteMessage.data.isNotEmpty().let {
            println(TAG + " Message data payload: " + remoteMessage.data)
            val message_id = remoteMessage.data["message_id"]?.toLong()?:0.toLong()
            val sender_id = remoteMessage.data["sender_id"]?.toLong()?:0.toLong()
            val receiver_id = remoteMessage.data["receiver_id"]?.toLong()?:0.toLong()
            val message_date = remoteMessage.data["message_date"]?.toLong()?:0.toLong()
            val message = remoteMessage.data["message"]?:""

            if(sender_id != 0L && receiver_id != 0L && message_date!=0L && message!=""){
                dao.insert(Message.Params(message_id,
                    sender_id, receiver_id, message, message_date
                ))
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