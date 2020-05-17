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
import kotlinx.coroutines.launch

class DialogViewModel(application: Application) : AndroidViewModel(application) {

    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    private val dao = MainDatabase.getDatabase(application).messageDao()
    val LiveDataRepository: MutableLiveData<List<Message.Params>> = MutableLiveData()
    private val liveData: MutableLiveData<ResponseMessage> = MutableLiveData()

    fun getData(friendId: Int): MutableLiveData<ResponseMessage>{
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val account = pref.getAccount()
        val idUser = account.idUser
        val token = account.token
        getMessages(idUser, friendId, token)
        return liveData
    }

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

    override fun onCleared() {
        super.onCleared()
        println("Метод очистки DialogVewModel!!")
    }
}