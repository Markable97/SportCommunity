package com.glushko.sportcommunity.data_layer.datasource

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager.Companion.ACCOUNT_TOKEN
import com.glushko.sportcommunity.presentation_layer.vm.DialogViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println(TAG + " From: ${remoteMessage.from}")
        val dao = MainDatabase.getDatabase(this).messageDao()
        remoteMessage.data.isNotEmpty().let {
            println(TAG + " Message data payload: " + remoteMessage.data)
            val messageId = remoteMessage.data["message_id"]?.toLong()?:0.toLong()
            val senderId = remoteMessage.data["sender_id"]?.toLong()?:0.toLong()
            val receiverId = remoteMessage.data["receiver_id"]?.toLong()?:0.toLong()
            val messageDate = remoteMessage.data["message_date"]?.toLong()?:0.toLong()
            val message = remoteMessage.data["message"]?:""

            if(senderId != 0L && receiverId != 0L && messageDate!=0L && message!=""){
                println("Отправляю данные в диалог")
                val intentForDialog = Intent(DialogViewModel.BROADCOAST_FILTER)
                intentForDialog.putExtra(ApiService.PARAM_SENDER_ID, senderId)
                intentForDialog.putExtra(ApiService.PARAM_RECEIVER_ID, receiverId)
                intentForDialog.putExtra(ApiService.PARAM_MESSAGE_DATE, messageDate)
                intentForDialog.putExtra(ApiService.PARAM_MESSAGE, message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intentForDialog)

                /*dao.insert(Message.Params(messageId,
                    senderId, receiverId, message, messageDate
                ))*/
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