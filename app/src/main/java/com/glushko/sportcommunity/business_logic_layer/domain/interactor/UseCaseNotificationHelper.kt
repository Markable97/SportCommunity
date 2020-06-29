package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import android.content.Context
import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.data_layer.repository.ChatsNotification
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.google.firebase.messaging.RemoteMessage

class UseCaseNotificationHelper(private val context: Context, private val remoteMessage: RemoteMessage) {
    private val dao = MainDatabase.getMessageDao(context)
    private val notificationDao = MainDatabase.getNotificationDao(context)
    private val pref = SharedPrefsManager.getSharedPrefsManager(context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE))

    fun addMessageInDatabase(){
        val messageType = remoteMessage.data["type_message"]?.toInt()?:100
        val messageId = remoteMessage.data["message_id"]?.toLong()?:0.toLong()
        val senderId = remoteMessage.data["sender_id"]?.toLong()?:0.toLong()
        val receiverId = remoteMessage.data["receiver_id"]?.toLong()?:0.toLong()
        val messageDate = remoteMessage.data["message_date"]?.toLong()?:0.toLong()
        val message = remoteMessage.data["message"]?:""
        val contactName = remoteMessage.data["contact_name"]?:""
        //val image = remoteMessage.data["image"]?:""

        if(messageType  != 100 && senderId != 0L && receiverId != 0L && messageDate!=0L){
            println("Вставляю данные в сервисе")



            dao.insert(
                Message.Params(messageId, messageType,
                    senderId, receiverId, message, messageDate
                ))
            val userId = pref.getAccount().idUser.toLong()
            //var contactId = 0L
            val contactId = if(userId == senderId){
                receiverId
            }else{
                senderId
            }
            val notificationCount = notificationDao.getNotificationChats(contactId) + 1
            notificationDao.setNotificationChats(ChatsNotification(contactId, notificationCount))
            val _message = if(message=="") "Фотография" else message
            dao.insertLastMessage(
                LastMessage.Params(messageId, contactId, messageType, contactName, senderId, receiverId, _message, messageDate,
                    notificationCount))
        }
    }

    fun addFriendshipInDatabase(){

    }
}