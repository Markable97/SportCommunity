package com.glushko.sportcommunity.data_layer.datasource

import android.content.Context
import android.util.Log
import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage
import com.glushko.sportcommunity.business_logic_layer.domain.Message
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
        val dao = MainDatabase.getMessageDao(this)
        remoteMessage.data.isNotEmpty().let {
            println(TAG + " Message data payload: " + remoteMessage.data)
            val messageType = remoteMessage.data["type_message"]?.toInt()?:0
            val messageId = remoteMessage.data["message_id"]?.toLong()?:0.toLong()
            val senderId = remoteMessage.data["sender_id"]?.toLong()?:0.toLong()
            val receiverId = remoteMessage.data["receiver_id"]?.toLong()?:0.toLong()
            val messageDate = remoteMessage.data["message_date"]?.toLong()?:0.toLong()
            val message = remoteMessage.data["message"]?:""
            val contactName = remoteMessage.data["contact_name"]?:""
            //val image = remoteMessage.data["image"]?:""

            if(messageType  != 0 && senderId != 0L && receiverId != 0L && messageDate!=0L){
                println("Вставляю данные в сервисе")

                dao.insert(Message.Params(messageId, messageType,
                    senderId, receiverId, message, messageDate
                ))
                val pref = SharedPrefsManager.getSharedPrefsManager(this.getSharedPreferences(this.packageName, Context.MODE_PRIVATE))
                val userId = pref.getAccount().idUser.toLong()
                var contactId = 0L
                if(userId == senderId){
                    contactId = receiverId
                }else{
                    contactId = senderId
                }
                val _message = if(message=="") "Фотография" else message
                dao.insertLastMessage(LastMessage.Params(messageId, contactId, messageType, contactName, senderId, receiverId, _message, messageDate))


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