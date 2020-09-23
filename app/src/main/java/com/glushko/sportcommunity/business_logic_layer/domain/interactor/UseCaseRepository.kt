package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.*
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import com.glushko.sportcommunity.data_layer.datasource.response.*
import com.glushko.sportcommunity.data_layer.repository.FriendshipNotification
import com.glushko.sportcommunity.data_layer.repository.MainDao
import com.glushko.sportcommunity.data_layer.repository.MessageDao
import com.glushko.sportcommunity.data_layer.repository.NotificationDao
import com.glushko.sportcommunity.presentation_layer.ui.home.HomeActivity
import io.reactivex.Observable
import io.reactivex.Single
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

    fun getFriendsList(dao: MainDao): List<Friend.Params>{
        return dao.getFriendsList()
    }

    suspend fun getFriends(
        param: Int,
        liveData: MutableLiveData<ResponseFriends>,
        dao: MainDao,
        dao_dop: NotificationDao
    ){
        try{
            val response =  NetworkService.makeNetworkService().getFriends(Friend.createMap(param)).await()
            dao.deleteFriends()
            if (response.friends_request.isNotEmpty()){
                val friendsRequest = mutableListOf<FriendshipNotification>()
                for(friend in response.friends_request){
                    friendsRequest.add(FriendshipNotification(friend.friend_id.toLong(), friend.user_name, friend.status_friend!!))
                }
                dao_dop.setNotificationFriend(friendsRequest)
            }else{
                dao_dop.deleteAllFriendsNotification()
            }
            if(response.friends.isNotEmpty() ){
                dao.insertFriends(response.friends)
                liveData.postValue(response)
            }
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
    //Методы под RxJava
    fun searchUser(text: String, type_find: String, team_id: Int): Observable<ResponseFriends>{
        return when(type_find){
            "friend" -> NetworkService.makeNetworkServiceRxJava().findUser(Friend.createMap(text, HomeActivity.USER_ID, type_find))
            "team" -> NetworkService.makeNetworkServiceRxJava().findUser(Friend.createMap(text, HomeActivity.USER_ID, type_find, team_id))
            else -> NetworkService.makeNetworkServiceRxJava().findUser(Friend.createMap(text, HomeActivity.USER_ID, type_find))
        }

    }

    fun deleteNotificationFriend(friend_id: Long, dao: NotificationDao): Single<Int> {

        return dao.deleteFiendsNotification(FriendshipNotification(friend_id, "Test", "test"))

    }

    fun deleteFriend(friend_id: Long, dao: MainDao): Single<Int>{
        return dao.deleteFriend(Friend.Params(friend_id.toInt(), "", "", "", 0, ""))
    }

    fun friendshipAction(user_id: Long, user_name: String, friend_id: Long, action: String, token: String): Observable<ResponseFriendship>{

        return NetworkService.makeNetworkServiceRxJava().friendshipAction(Friend.createMap(user_id, user_name, friend_id, action, token))
    }

    fun getFootballLeagues(): Observable<ResponseFootballLeagues>{
        return NetworkService.makeNetworkServiceRxJava().getFootballLeagues()
    }

    fun getFootballDivisions(league_id: Int): Observable<ResponseFootballDivisions>{
        return NetworkService.makeNetworkServiceRxJava().getFootballDivisions(ResponseFootballDivisions.createMap(league_id))
    }

    fun getFootballTeams(division_id: Int): Observable<ResponseFootballTeams>{
        return NetworkService.makeNetworkServiceRxJava().getFootballTeams(ResponseFootballTeams.createMap(division_id))
    }

    fun createTeam(user_id: Int, team_id: String): Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().createTeam(BaseResponse.createMap(user_id, team_id))
    }

    fun inviteInTeam(user_id: Long, team_id: Int, team_name: String, type_invitation: String): Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().inviteInTeam(BaseResponse.createMap(user_id, team_id, team_name, type_invitation))
    }
    fun getSquadList(team_id: Int, user_id: Long): Observable<ResponseSquadTeamList>{
        return NetworkService.makeNetworkServiceRxJava().getSquadTeamList(ResponseSquadTeamList.createMap(team_id, user_id))
    }

    fun compareUsers(type_compare: Int,team_id: Int, user_id: Long, player_id: Long) : Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().compareUsers(BaseResponse.createMap(type_compare,user_id, player_id, team_id) )
    }

    fun getNotification(user_id: Long, token: String) : Observable<ResponseNotifications>{
        return NetworkService.makeNetworkServiceRxJava().getNotifications(Notification.createMap(user_id, token))
    }
}