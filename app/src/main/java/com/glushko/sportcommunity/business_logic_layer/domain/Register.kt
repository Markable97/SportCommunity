package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCase
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import retrofit2.await

data class Register(
    val email: String,
    val name: String,
    val password: String
){
     fun sendData(data: MutableLiveData<String>, useCase: UseCase = UseCase()){
         val response = useCase.request {
             var request =  NetworkService.makeNetworkService().register(createRegisterMap(email, name, password, "1234", 0))
             try{
                 var answer = request.await()
                 println("Ответ от сервера ${answer.message} ${answer.success}")
                 data.postValue(answer.message)
                 answer
             }catch (ce: Exception){
                 println("ERRRRRROOOOOOORRRRRR")
                 val responseError = BaseResponse(-1, "Not Connect Server")
                 data.postValue(responseError.message)
                 responseError
             } }

        //data.postValue(response.message)
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