package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.TeamsUserInfo
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMainPage
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    //val person: LiveData<Person>
    val liveData:MutableLiveData<ResponseMainPage> = MutableLiveData()
    //val LiveDataRepository: LiveData<List<TeamsUserInfo.Params>>
    private val mainDao = MainDatabase.getDatabase(application).mainDao()

    init{
        //LiveDataRepository = useCaseRepository.mainPage(mainDao)
    }

    fun getData():MutableLiveData<ResponseMainPage>{
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val idUser = pref.getAccount().idUser

        getMainPage(idUser)
        return liveData
    }

    fun getData(id: Long):MutableLiveData<ResponseMainPage>{
        getMainPage(id.toInt())
        return liveData
    }

    private fun getMainPage(user_id: Int = 0){
        viewModelScope.launch {
            try {
                useCaseRepository.mainPage(user_id, liveData, mainDao)
            }catch(err: NetworkErrors){
                println(err.message)
                liveData.postValue(
                    ResponseMainPage(
                        -1,
                        "Server Error"
                    )
                )
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        println("Метод очистки ProfileVewModel!!")
    }
}