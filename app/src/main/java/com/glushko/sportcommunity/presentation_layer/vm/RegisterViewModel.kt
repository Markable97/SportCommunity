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
import retrofit2.awaitResponse
import java.lang.Exception

class RegisterViewModel : ViewModel() {

    var liveData: MutableLiveData<String> = MutableLiveData()

    fun getData(): MutableLiveData<String>{
        return liveData
    }

    fun registerUser(email: String, name: String, password: String) {
        val register = Register(email, name, password)
        println("Значение для регистраици $register")
        GlobalScope.launch(Dispatchers.IO) {
            var request =  NetworkService.makeNetworkService().register(createRegisterMap(email, name, password, "1234", 0))
            try{
                var answer = request.await()
                println("Ответ от сервера ${answer.message} ${answer.success}")
                liveData.postValue(answer.message)
            }catch (ce: Exception){
                println("ERRRRRROOOOOOORRRRRR")
                liveData.postValue("Пошел в жопу ниче не работает")
            }


        }

        //liveData.postValue("Привет из Корутины через 3 секунлу")
    }

    private fun createRegisterMap(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_NAME, name)
        map.put(ApiService.PARAM_PASSWORD, password)
        map.put(ApiService.PARAM_TOKEN, token)
        map.put(ApiService.PARAM_USER_DATE, userDate.toString())
        return map
    }
}