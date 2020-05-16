package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.*
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFriends
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseLogin
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMainPage
import com.glushko.sportcommunity.data_layer.repository.MainDao
import retrofit2.await

class UseCaseRepository {
    //val personInfo = mainDao.getPerson()

    var mainPageInfo: LiveData<List<TeamsUserInfo.Params>> = MutableLiveData()
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

    fun mainPage(dao: MainDao): LiveData<List<TeamsUserInfo.Params>>{
        mainPageInfo = dao.getMainPage()
        return  mainPageInfo
        }

    suspend fun mainPage(param: Int, livaData: MutableLiveData<ResponseMainPage>, dao: MainDao){
        try{
            val response = NetworkService.makeNetworkService().main_page(TeamsUserInfo.createMap(param)).await()
            dao.insertMainPage(response.teamsUserinfo)
            //вытащить список всех id
            val listId = mutableListOf<Long>()
            for(info in response.teamsUserinfo){
                listId.add(info.team_id.toLong())
            }
            dao.deleteBadInfoMainPage(listId.toList())
            livaData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }
    }

    suspend fun getfriends(param: Int, liveData: MutableLiveData<ResponseFriends>){
        try{
            val response =  NetworkService.makeNetworkService().getFriends(Friend.createMap(param)).await()
            liveData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }
    }
}