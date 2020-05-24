package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseLastMessage
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatsViewModel(application: Application) : AndroidViewModel(application) {

    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    private val dao = MainDatabase.getMessageDao(application)
    val liveDataRepository: LiveData<List<LastMessage.Params>>
    private val liveData: MutableLiveData<ResponseLastMessage> = MutableLiveData()

    init{
        liveDataRepository = dao.getLastMessage()
    }

    fun getData(): MutableLiveData<ResponseLastMessage>{
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val idUser = pref.getAccount().idUser
        val token = pref.getToken()
        getLastMessage(idUser.toLong(), token)
        return liveData
    }

    private fun getLastMessage(userId: Long, token: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                useCaseRepository.getLastContactMessage(userId, token, liveData, dao)
            }catch (err: NetworkErrors){
                println(err.message)
                liveData.postValue(
                    ResponseLastMessage(
                    -1,
                    "Server Error")
                )
            }
        }
    }
}