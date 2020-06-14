package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.data_layer.repository.ChatsNotification
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationDrawerViewModel(application: Application): AndroidViewModel(application) {

    private val dao = MainDatabase.getNotificationDao(application)

    val chatsLiveData: LiveData<List<ChatsNotification>>
    //private val notificationLiveData: MutableLiveData<> = MutableLiveData()
    init {
        chatsLiveData = dao.getNotificationChatsLiveData()
    }

    fun deleteNotificationChats(){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteNotificationChats()
        }
    }

    fun deleteChooseNotificationChat(contactId: Long){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteNotificationChats(contactId)
        }
    }
}