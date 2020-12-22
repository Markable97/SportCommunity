package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.FileUtils
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLDecoder

class DialogViewModel(application: Application, val friend_id: Long, type_dialog: Int) : AndroidViewModel(application) {

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
        liveDataRepository = useCaseRepository.getMessages(dao, idUser.toLong(), friend_id, type_dialog)
    }


    fun getData(friendId: Long, type_dialog: Int): MutableLiveData<ResponseMessage>{
        //whileGetMessages(friendId.toLong())

        getMessages(idUser, friend_id, token, type_dialog)
        return liveData
    }

    /*fun getDataRepositiry(friendId: Int): LiveData<List<Message.Params>>{
        LiveDataRepository = useCaseRepository.getMessages(dao, idUser.toLong(), friendId.toLong())

        return LiveDataRepository
    }*/

    private fun getMessages(userId: Int, friendId: Long, token: String, type_dialog: Int){
        viewModelScope.launch {
            try{
                val params = Message.Params(message_id = 0,message_type = 1, sender_id = userId.toLong(),receiver_id = friendId)
                useCaseRepository.getMessages(params, token, liveData,dao, type_dialog)
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

    fun sendMessage(friendId: Long, message: String, photoFile: File?, photoUri: Uri?,messageType: Int = 1 ){
        viewModelScope.launch(Dispatchers.IO) {
            try{

                val params = Message.Params(message_id = 0,sender_id = idUser.toLong(),message_type = messageType, receiver_id = friendId, message = URLDecoder.decode(message, "UTF-8"))
                println("Send message to server $params")
                val body: MultipartBody.Part? = prepareFilePart(photoFile, photoUri)
                useCaseRepository.sendMessage(params, token, body, liveData, dao)
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

    private fun  prepareFilePart(photoFile: File?, photoUri: Uri?): MultipartBody.Part?{
        if(photoFile != null && photoUri != null){
            val requestFile = RequestBody.create(MediaType.parse(
                getApplication<Application>().contentResolver.getType(photoUri))
                ,photoFile)
            return MultipartBody.Part.createFormData("test_image", photoFile.name, requestFile)
        }
        return null
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