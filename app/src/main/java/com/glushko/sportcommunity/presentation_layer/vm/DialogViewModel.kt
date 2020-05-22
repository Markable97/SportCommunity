package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMessage
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLDecoder

class DialogViewModel(application: Application, private val friend_id: Long) : AndroidViewModel(application) {

    companion object{
        const val TAG = "DialogViewModel"
        const val BROADCOAST_FILTER = "return.firebasemessage.service"
    }

    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    private val dao = MainDatabase.getMessageDao(application)
    val liveDataRepository: LiveData<List<Message.Params>>
    private val liveData: MutableLiveData<ResponseMessage> = MutableLiveData()
    private val pref =  SharedPrefsManager(getApplication<Application>().
        getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
    private val account = pref.getAccount()
    private val idUser = account.idUser
    private val token = account.token
    private lateinit var broadCoast: BroadcastReceiver

    init {
        liveDataRepository = useCaseRepository.getMessages(dao, idUser.toLong(), friend_id)
    }


    fun getData(friendId: Int): MutableLiveData<ResponseMessage>{
        //whileGetMessages(friendId.toLong())

        getMessages(idUser, friendId, token)
        return liveData
    }

    /*fun getDataRepositiry(friendId: Int): LiveData<List<Message.Params>>{
        LiveDataRepository = useCaseRepository.getMessages(dao, idUser.toLong(), friendId.toLong())

        return LiveDataRepository
    }*/

    private fun getMessages(userId: Int, friendId: Int, token: String){
        viewModelScope.launch {
            try{
                val params = Message.Params(message_id = 0,sender_id = userId.toLong(),receiver_id = friendId.toLong())
                useCaseRepository.getMessages(params, token, liveData,dao)
            }catch (err: NetworkErrors){
                println(err.message)
                liveData.postValue(
                    ResponseMessage(
                        -1,
                        "Server Error")
                )
            }
        }
    }

    fun sendMessage(friendId: Int, message: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{

                val params = Message.Params(message_id = 0,sender_id = idUser.toLong(),receiver_id = friendId.toLong(), message = URLDecoder.decode(message, "UTF-8"))
                println("Send message to server $params")
                useCaseRepository.sendMessage(params, token, liveData, dao)
            }catch (err: NetworkErrors){
                println(err.message)
                liveData.postValue(
                    ResponseMessage(
                        -1,
                        err.message?:"Ошибка сервера")
                )
            }
        }
    }

    private fun whileGetMessages(friend_id: Long){
        println("Start?")
        viewModelScope.launch(Dispatchers.IO){
            while(true){
                delay(1000)
                //LiveDataRepository.postValue(useCaseRepository.getMessages(dao, idUser.toLong(), friend_id))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("Метод очистки DialogVewModel!!")
    }
}