package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCase
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import retrofit2.await

class Login(val useCase: UseCase = UseCase()) {

    data class Params(val email: String, val password: String)

    fun sendData(params: Params, data: MutableLiveData<String>){
        val response = useCase.request{
            var request = NetworkService.makeNetworkService().login(createLoginMap(params.email, params.password))
            try {
                val answer = request.await()
                data.postValue(answer.message)
                answer
            }catch (ex: Exception){
                println("${ex.message} \nERRRRRROOOOOOORRRRRR")
                val responseError = BaseResponse(-1, "Server Error")
                data.postValue(responseError.message)
                responseError
            }

        }
    }

    private fun createLoginMap(
        email: String,
        password: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_PASSWORD, password)
        return map
    }
}