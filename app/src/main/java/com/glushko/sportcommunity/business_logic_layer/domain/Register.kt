package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCase
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.await

class Register(
    val useCase: UseCase = UseCase()
){

    data class Params( var email: String,
                       var name: String,
                       var password: String,
                       var token: String)

     fun sendData(params: Params, data: MutableLiveData<String> ){
         var response = useCase.request {
             var token = ""
             var request =  NetworkService.makeNetworkService().register(createRegisterMap(params.email, params.name, params.password,params.token))
             try{
                 var answer = request.await()
                 println("Ответ от сервера ${answer.message} ${answer.success}")
                 data.postValue(answer.message)
                 answer
             }catch (ce: Exception){
                 println("ERRRRRROOOOOOORRRRRR")
                 val responseError = BaseResponse(-1, "Server error")
                 data.postValue(responseError.message)
                 responseError
             } }

        //data.postValue(response.message)
     }
    private fun createRegisterMap(
        email: String,
        name: String,
        password: String,
        token: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_NAME, name)
        map.put(ApiService.PARAM_PASSWORD, password)
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
 }