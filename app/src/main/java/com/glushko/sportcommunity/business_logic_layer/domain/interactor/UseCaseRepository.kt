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

    fun getMessages(dao: MessageDao, sender_id_id: Long, receiver_id: Long, type_dialog: Int): LiveData<List<Message.Params>>{
        return if(type_dialog == 0){
            dao.getMessages(sender_id_id, receiver_id)
        }else{
            dao.getMessages(receiver_id.toInt())
        }
    }

    suspend fun getMessages(params: Message.Params, token: String, livData: MutableLiveData<ResponseMessage>,dao: MessageDao, type_dialog: Int){
        try{
            println("$params")
            val response = if(type_dialog == 0) {
                NetworkService.makeNetworkService().getMessages(Message.createMap(params.sender_id, params.receiver_id, token)).await()
            }else{
                NetworkService.makeNetworkService().getMessagesTeam(Message.createMap(params.receiver_id, token)).await()
            }

            println("UseCase: getMessages() ${response.success} ${response.message} ${response.messages}")
            //println("before insert ${dao.getCntMessage(params.receiver_id.toInt())}")
            dao.insert(response.messages)
            //println("after insert ${dao.getCntMessage(params.receiver_id.toInt())}")
            livData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }
        finally {
            //liveDataRepository.postValue(getMessages(dao,params.sender_id, params.receiver_id))
        }
    }

    suspend fun sendMessage(params: Message.Params, token: String, file: MultipartBody.Part?, livData: MutableLiveData<ResponseMessage>, dao: MessageDao, type_dialog: Int){
         try{
             val response =  if(file != null) {
                 if(type_dialog == 0){
                     NetworkService.makeNetworkService().sendMessage(Message.createMapFile(params.sender_id, params.receiver_id, token, params.message, params.message_type), file).await()
                 }else{
                     NetworkService.makeNetworkService().sendMessageTeam(Message.createMapFile(params.sender_id, params.receiver_id, token, params.message, params.message_type), file).await()
                 }
             }else{
                 if(type_dialog == 0){
                     NetworkService.makeNetworkService().sendMessage(Message.createMap(params.sender_id, params.receiver_id, token, params.message, params.message_type)).await()
                 }else{
                     NetworkService.makeNetworkService().sendMessageTeam(Message.createMap(params.sender_id, params.receiver_id, token, params.message, params.message_type)).await()
                 }
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
            dao.deleteBadLastMessages(response.lastMessages.map{ it.contact_id })
            liveData.postValue(response)
        }catch (cause: Throwable){
            println("Error!!!!${cause.message}")
            throw NetworkErrors(cause.message?:"Сервер не отвечает", cause)
        }

    }
    //Методы под RxJava

    fun getTeamInfo(user_id: Long, team_id: Long, token: String): Observable<ResponseMainPage>{
        return NetworkService.makeNetworkServiceRxJava().getTeamInfo(TeamsUserInfo.createMap(user_id, team_id, token))
    }

    fun searchUser(text: String, type_find: String, team_id: Int): Observable<ResponseFriends>{
        return when(type_find){
            "friend" -> NetworkService.makeNetworkServiceRxJava().findUser(Friend.createMap(text, HomeActivity.USER_ID!!, type_find))
            "team" -> NetworkService.makeNetworkServiceRxJava().findUser(Friend.createMap(text, HomeActivity.USER_ID!!, type_find, team_id))
            else -> NetworkService.makeNetworkServiceRxJava().findUser(Friend.createMap(text, HomeActivity.USER_ID!!, type_find))
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

    fun getFootballTeamMatches(team_id: Long): Observable<ResponseFootballMatches>{
        return NetworkService.makeNetworkServiceRxJava().getFootballMatchesTeam(ResponseFootballMatches.createMap(team_id))
    }

    fun getTournamentTableFootball(division_id: Int, season_id: Int, team_id: Long): Observable<ResponseTournamentTableFootball>{
        return NetworkService.makeNetworkServiceRxJava().getTournamentTableFootball(ResponseTournamentTableFootball.createMap(division_id, season_id, team_id))
    }

    fun createTeam(user_id: Int, team_id: String): Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().createTeam(BaseResponse.createMap(user_id, team_id))
    }

    fun inviteInTeam(user_id: Long, team_id: Int, team_name: String, type_invitation: String, token: String): Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().inviteInTeam(BaseResponse.createMap(user_id, team_id, team_name, type_invitation, token))
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

    fun getEventsTeam(user_id: Long, team_id: Long, token: String): Observable<ResponseEventsTeam>{
        return NetworkService.makeNetworkServiceRxJava().getEventsTeam(Event.createMap(user_id, team_id, token))
    }

    fun deleteEvent(user_id: Long, event_id: Long, token: String): Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().deleteEvent(Event.createMap(token, user_id, event_id))
    }

    fun createEventTeam(user_id: Long, token: String, team_id: Long, team_name: String, event_name: String, event_date: String?, event_location: String?,
                        positive_name: String?, negative_name: String?, neutral_name: String?): Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().createEvent(Event.createMap(user_id, token, team_id, team_name, event_name, event_date, event_location, positive_name, negative_name, neutral_name))
    }

    fun modifyChoiceEvent(user_id: Long, token: String, event_id: Long, choice_mode: String, choice: String): Observable<BaseResponse>{
        return NetworkService.makeNetworkServiceRxJava().modifyChoiceEvent(Event.createMap(user_id, token, event_id, choice_mode, choice))
    }

    fun getPlayersInMath(match_id: Long): Observable<ResponsePlayersInMatch>{
        return NetworkService.makeNetworkServiceRxJava().getPlayersInMatch(ResponsePlayersInMatch.createMap(match_id))
    }
}