package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFriends
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import kotlinx.coroutines.launch

class FriendsViewModel(application: Application) : AndroidViewModel(application) {
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    private val liveData: MutableLiveData<ResponseFriends> = MutableLiveData()

    fun getData(): MutableLiveData<ResponseFriends>{
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val idUser = pref.getAccount().idUser
        getFriends(idUser)
        return liveData
    }

    private fun getFriends(idUser: Int) {
        viewModelScope.launch {
            try {
                useCaseRepository.getFriends(idUser, liveData)
            }catch (err: NetworkErrors){
                println(err.message)
                liveData.postValue(ResponseFriends(
                    -1,
                    "Server Error"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        println("Метод очистки FriendsVewModel!!")
    }
}