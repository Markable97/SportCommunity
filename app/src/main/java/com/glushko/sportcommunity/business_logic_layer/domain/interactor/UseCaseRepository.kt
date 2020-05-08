package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.Login
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import com.glushko.sportcommunity.data_layer.datasource.ResponseLogin
import com.glushko.sportcommunity.data_layer.repository.MainDao
import retrofit2.await
import kotlin.Exception

class UseCaseRepository {
    //val personInfo = mainDao.getPerson()

    suspend fun loginUser(params: Login.Params, data: MutableLiveData<ResponseLogin>){
        try{
            println("Выполняю запрос")
            val response = NetworkService.makeNetworkService().login(Login.createLoginMap(email = params.email, password = params.password, token = params.token)).await()
            data.postValue(response)
        }catch (cause:Throwable){
            println("Error!!!! ${cause.message}")
            throw NetworkErrors(cause.message?: "Сервер не отвечает", cause)
        }
    }

    suspend fun registerUser(params: Register.Params, livaData: MutableLiveData<String>){
        try {
            val response = NetworkService.makeNetworkService().register(Register.createRegisterMap(params.email, params.name, params.password,params.token)).await()
            livaData.postValue(response.message)
        }catch (cause: Throwable){
            println("Error!!!! ${cause.message}")
            throw NetworkErrors(cause.message?: "Сервер не отвечает", cause)
        }
    }
}