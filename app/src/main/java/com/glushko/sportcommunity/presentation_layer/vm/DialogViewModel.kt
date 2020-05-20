package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMessage
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.Timeout
import java.net.URLDecoder

class DialogViewModel(application: Application) : AndroidViewModel(application) {

    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    private val dao = MainDatabase.getDatabase(application).messageDao()
    val LiveDataRepository: MutableLiveData<List<Message.Params>> = MutableLiveData()
    private val liveData: MutableLiveData<ResponseMessage> = MutableLiveData()
    private val pref =  SharedPrefsManager(getApplication<Application>().
        getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
    private val account = pref.getAccount()
    private val idUser = account.idUser
    private val token = account.token


    fun getData(friendId: Int): MutableLiveData<ResponseMessage>{
        whileGetMessages(friendId.toLong())
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
                useCaseRepository.getMessages(params, token, liveData, LiveDataRepository,dao)
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
                useCaseRepository.sendMessage(params, token, liveData, LiveDataRepository, dao)
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
                LiveDataRepository.postValue(useCaseRepository.getMessages(dao, idUser.toLong(), friend_id))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("Метод очистки DialogVewModel!!")
    }
}