package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.ResponseMainPage
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val useCaseRepository: UseCaseRepository
    //val person: LiveData<Person>

    init{
        //val mainDao = MainDatabase.getDatabase(application).mainDao()
        //useCaseRepository = UseCaseRepository(mainDao)
        //person = useCaseRepository.personInfo
        useCaseRepository = UseCaseRepository()
    }
    val liveData:MutableLiveData<ResponseMainPage> = MutableLiveData()

    fun getData():MutableLiveData<ResponseMainPage>{
        getMainPage()
        return liveData
    }

    fun getMainPage(user_id: Int = 0){
        viewModelScope.launch {
            try {
                useCaseRepository.mainPage(user_id, liveData)
            }catch(err: NetworkErrors){
                println(err.message)
                liveData.postValue(ResponseMainPage(-1, "Server Error"))
            }
        }
    }
}