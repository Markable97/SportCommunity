package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.*
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import com.glushko.sportcommunity.data_layer.datasource.response.*
import com.glushko.sportcommunity.data_layer.repository.MainDao
import com.glushko.sportcommunity.data_layer.repository.MessageDao
import com.glushko.sportcommunity.data_layer.repository.NotificationDao
import okhttp3.MultipartBody
import retrofit2.await
import java.lang.Exception

class UseCaseRepository {
    //val personInfo = mainDao.getPerson()

    //var mainPageInfo: List<TeamsUserInfo.Params> = listOf()
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

    fun mainPage(dao: MainDao):LiveData<List<TeamsUserInfo.Params>>{
        val mainPageInfo = dao.getMainPage()
        return  mainPageInfo
        }



    suspend fun mainPage(param: Int, livaData: MutableLiveData<ResponseMainPage>,dao: MainDao){
        try{
            val response = NetworkService.makeNetworkService().mainPage(TeamsUserInfo.createMap(param)).await()
            /*dao.insertMainPage(response.teamsUserinfo)
            //вытащить список всех id
            val listId = mutableListOf<Long>()
            for(info in response.teamsUserinfo){
                listId.add(info.team_id.toLong())
            }
            dao.deleteBadInfoMainPage(listId.toList())*/
            livaData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }finally {
            //liveDataRepository.postValue(mainPage(dao))
        }
    }

    fun getFriends(dao: MainDao): LiveData<List<Friend.Params>>{
        return dao.getFriends()
    }

    suspend fun getFriends(
        param: Int,
        liveData: MutableLiveData<ResponseFriends>,
        dao: MainDao
    ){
        try{
            val response =  NetworkService.makeNetworkService().getFriends(Friend.createMap(param)).await()
            dao.insertFriends(response.friends)
            liveData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }finally {
            //liveDataRepository.postValue(getFriends(dao))
        }
    }

    fun getMessages(dao: MessageDao, sender_id_id: Long, receiver_id: Long): LiveData<List<Message.Params>>{
        return dao.getMessages(sender_id_id, receiver_id)
    }

    suspend fun getMessages(params: Message.Params, token: String, livData: MutableLiveData<ResponseMessage>,dao: MessageDao){
        try{
            println("$params")
            val response = NetworkService.makeNetworkService().getMessages(Message.createMap(params.sender_id, params.receiver_id, token)).await()
            println("UseCase: getMessages() ${response.success} ${response.message} ${response.messages}")
            dao.insert(response.messages)
            livData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }
        finally {
            //liveDataRepository.postValue(getMessages(dao,params.sender_id, params.receiver_id))
        }
    }

    suspend fun sendMessage(params: Message.Params, token: String, file: MultipartBody.Part?, livData: MutableLiveData<ResponseMessage>, dao: MessageDao){
         try{
             val response =  if(file != null) {
                 NetworkService.makeNetworkService().sendMessage(Message.createMapFile(params.sender_id, params.receiver_id, token, params.message, params.message_type), file).await()
             }else{
                 NetworkService.makeNetworkService().sendMessage(Message.createMap(params.sender_id, params.receiver_id, token, params.message, params.message_type)).await()
             }
             if(response.messages.isEmpty()){
                 throw NetworkErrors(response.message, Exception())
             }
             //Создаем локальную переменную собщения, в которой не хватает всего лишь времени
             val local = params
             //Получаем в ответе от сервера время и вставляем в локальную переменную
             local.message_date = response.messages.first().message_date
             //А теперь вставляем в бд
             println("Вставляю в локальную бд")
             dao.insert(local)
             livData.postValue(response)
             //liveDataRepository.postValue(getMessages(dao, params.sender_id, params.receiver_id))
         }catch (cause: Throwable){
             println("Error!!!!${cause.message}")
             throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
         }
    }

    suspend fun getLastContactMessage(userId: Long, token: String, liveData: MutableLiveData<ResponseLastMessage>, dao: MessageDao){
        try{
            val response =  NetworkService.makeNetworkService().getLastContactMessage(LastMessage.createMap(userId, token)).await()
            println(response.message)
            if(response.lastMessages.isEmpty()){
                throw NetworkErrors(response.message, Exception())
            }
            dao.insertLastMessage(response.lastMessages.toList())
            dao.updateLastMessageNotification()
            liveData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }

    }
}