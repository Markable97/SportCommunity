package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.business_logic_layer.domain.Notification
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.data_layer.repository.ChatsNotification
import com.glushko.sportcommunity.data_layer.repository.FriendshipNotification
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.home.HomeActivity
import com.google.firebase.messaging.RemoteMessage

class UseCaseNotificationHelper(private val context: Context, private val remoteMessage: RemoteMessage) {

    companion object{
        const val TYPE_OPEN = "type_open_fragment"
        const val TYPE_DIALOG = "type_dialog"
        const val OPEN_NOTIFICATIONS = "notifications"
        const val OPEN_FRIENDS = "friends"
        const val OPEN_DIALOG = "message"
    }

    private val messageDao = MainDatabase.getMessageDao(context)
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
        val typeDialog = remoteMessage.data["type_dialog"]?.toInt()?:0
        //val image = remoteMessage.data["image"]?:""

        if(messageType  != 100 && senderId != 0L && receiverId != 0L && messageDate!=0L){
            println("Вставляю данные в сервисе")



            messageDao.insert(
                Message.Params(messageId, messageType,
                    senderId, receiverId, message, messageDate
                ))
            val userId = pref.getAccount().idUser.toLong()
            //var contactId = 0L
            val contactId = if(typeDialog == 100){
                receiverId
            }else {
                if (userId == senderId) {
                    receiverId
                } else {
                    senderId
                }
            }
            val notificationCount = notificationDao.getNotificationChats(contactId) + 1
            notificationDao.setNotificationChats(ChatsNotification(contactId, notificationCount))
            val _message = if(message=="") "Фотография" else message
            messageDao.insertLastMessage(
                LastMessage.Params(messageId, contactId, messageType, contactName, senderId, receiverId, _message, messageDate,
                    notificationCount, typeDialog))
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(TYPE_OPEN, OPEN_DIALOG)
            intent.putExtra(TYPE_DIALOG, typeDialog)
            intent.putExtra(ApiService.PARAM_USER_ID, contactId)
            createNotification("Сообщение от $contactName", message, intent)
        }
    }

    fun addFriendshipInDatabase(){
        //val notificationDao = MainDatabase.getNotificationDao(context)
        val action = remoteMessage.data["action_friendship"]?:""
        val contactId = remoteMessage.data["contact_id"]?.toLong()?:0L
        val contactName = remoteMessage.data["contact_name"]?:""
        println("Вставляю данные в сервисе $contactName $action")
        println("OLD: ${notificationDao.getFriendsNotificationList()}")
        val friendshipNotification = FriendshipNotification(contactId, contactName, action)
        var message: String? = null
        try{
            when(action){
                "head_request" ->{
                    notificationDao.setNotificationFriend(friendshipNotification)
                    message = "$contactName хочет добавить Вас в друзья"
                }
                "reject_request" -> {notificationDao.deleteNotificationFriend(friendshipNotification.contact_id)}
                "accept_request" ->{
                    message = "$contactName подтвердил заявку в друзья"
                }
            }
            println("Заявка вставлена")
            println("NEW: ${notificationDao.getFriendsNotificationList()}")
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(TYPE_OPEN, OPEN_FRIENDS)
            message?.let {
                createNotification("Запрос на дружбу", it, intent)
            }

        }catch (ex: Exception){
            println("Ошмбка вставки заявкив  друзья ${ex.message}")
        }
    }

    fun addNotification(){

        val notification_id = remoteMessage.data["notification_id"]?.toLong()?:0L
        val type_invitation = remoteMessage.data["type_invitation"]?:""
        val team_id = remoteMessage.data["team_id"]?.toInt()?:0
        val team_name = remoteMessage.data["team_name"]?:""
        val notification = Notification.Params(notification_id, type_invitation, team_id, team_name)
        when(type_invitation){
            "request" -> notificationDao.insertNotification(notification)
        }
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(TYPE_OPEN, OPEN_NOTIFICATIONS)
        createNotification("Пришлащение в команду", "Команда $team_name хочет видеть Вас в своих рядах", intent)
    }


    private fun createNotification(tittle: String, message: String, intent: Intent){

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val channelID = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(tittle)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        //val notificationManager: NotificationManagerCompat  =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManagerCompat
        val  notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, "Channel readable", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())

    }


}