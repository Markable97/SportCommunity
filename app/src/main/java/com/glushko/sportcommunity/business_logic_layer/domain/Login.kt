package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCase
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import com.glushko.sportcommunity.data_layer.datasource.ResponseLogin
import retrofit2.await

class Login(val useCase: UseCase = UseCase()) {

    data class Params(val email: String, val password: String)

    fun sendData(params: Params, data: MutableLiveData<ResponseLogin>){
        useCase.request{
            var request = NetworkService.makeNetworkService().login(createLoginMap(params.email, params.password))
            try {
                //println("Получаю токен типа ${useCase.getToken()}")
                val answer = request.await()
                println("Данные пришли  в Login ${answer.success}, ${answer.message}, ${answer.nameUser}")
                data.postValue(answer)
                answer
            }catch (ex: Exception){
                println("${ex.message} \nERRRRRROOOOOOORRRRRR")
                val responseError = ResponseLogin(-1, "Server Error", "Err")
                data.postValue(responseError)
                responseError
            }

        }
    }

    /*fun sendData(params: Register.Params): BaseResponse{
        println("sendData on Login.kt = $params")
        var message: String = ""
        val response = useCase.request{
            var request = NetworkService.makeNetworkService().login(createLoginMap(params.email, params.password))
            try {
                val answer = request.await()
                answer
            }catch (ex: Exception){
                println("${ex.message} \nERRRRRROOOOOOORRRRRR")
                val responseError = BaseResponse(-1, "Server Error")
                responseError
            }
        }
        println("return sendData on Login.kt ={${response.message}}")
        return response
    }*/

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