package com.glushko.sportcommunity.presentation_layer.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.NetworkService

import kotlinx.coroutines.*
import retrofit2.await
import java.lang.Exception

class AccountViewModel : ViewModel() {

    var liveData: MutableLiveData<String> = MutableLiveData()
    private val register = Register()
    fun getData(): MutableLiveData<String>{
        return liveData
    }

    fun registerUser(email: String, name: String, password: String) {
        val registerParam = Register.Params(email, name, password)
        println("Значение для регистраици $register")
        val response = register.sendData(registerParam, liveData)

        /*GlobalScope.launch(Dispatchers.IO) {
            var request =  NetworkService.makeNetworkService().register(createRegisterMap(email, name, password, "1234", 0))
            try{
                var answer = request.await()
                println("Ответ от сервера ${answer.message} ${answer.success}")
                liveData.postValue(answer.message)
            }catch (ce: Exception){
                println("ERRRRRROOOOOOORRRRRR")
                liveData.postValue("Пошел в жопу ниче не работает")
            }


        }*/

        //liveData.postValue("Привет из Корутины через 3 секунлу")
    }

    override fun onCleared() {
        super.onCleared()
        register.useCase.cancel()
    }
}