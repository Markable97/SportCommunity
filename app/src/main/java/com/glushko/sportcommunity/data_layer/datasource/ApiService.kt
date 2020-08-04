package com.glushko.sportcommunity.data_layer.datasource

import com.glushko.sportcommunity.data_layer.datasource.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object{ //Что то типо статических вспомогательных классов
        //Methods
        const val REGISTER = "Register"
        const val LOGIN = "Login"
        const val MAIN_PAGE = "UserMainPage"
        const val GET_FRIENDS = "GetFriends"
        const val GET_MESSAGES = "GetMessages"
        const val SEND_MESSAGE = "SendMessage"
        const val LAST_MESSAGE_CONTACT = "GetLastContactMessage"
        const val FIND_USER = "FindPeople"
        const val FRIENDSHIP = "Friendship"
        const val GET_FOOTBALL_LEAGUES = "GetFootballLeague"
        const val GET_FOOTBALL_DIVISIONS = "GetFootballDivisions"
        const val GET_FOOTBALL_TEAMS = "GetFootballTeams"
        const val CREATE_TEAM = "CreateTeam"
        //params
        const val PARAM_EMAIL = "email"
        const val PARAM_PASSWORD = "password"
        const val PARAM_NAME = "name"
        const val PARAM_TOKEN = "token"
        const val PARAM_MESSAGE_TYPE = "type_message"
        const val PARAM_MESSAGE_DATE = "user_date"
        const val PARAM_USER_ID = "user_id"
        const val PARAM_FRIEND_ID = "friend_id"
        const val PARAM_SENDER_ID = "sender_id"
        const val PARAM_RECEIVER_ID = "receiver_id"
        const val PARAM_MESSAGE = "message"
        const val PARAM_USER_NAME = "user_name"
        const val PARAM_FRIENDSHIP_ACTION = "action"
        const val PARAM_FOOTBALL_LEAGUE_ID = "league_id"
        const val PARAM_FOOTBALL_DIVISION_ID = "division_id"
        const val PARAM_TEAM_ID = "team_id"
        const val PARAM_TYPE_FIND_USER = "type_find"
    }

    @FormUrlEncoded
    @POST(REGISTER)
    fun register(@FieldMap params: Map<String, String>) : Call<BaseResponse>

    @FormUrlEncoded
    @POST(LOGIN)
    fun login(@FieldMap params: Map<String, String>) : Call<ResponseLogin>

    @FormUrlEncoded
    @POST(MAIN_PAGE)
    fun mainPage(@FieldMap params: Map<String, String>):Call<ResponseMainPage>

    @FormUrlEncoded
    @POST(GET_FRIENDS)
    fun getFriends(@FieldMap params: Map<String, String>):Call<ResponseFriends>


    @FormUrlEncoded
    @POST(FRIENDSHIP)
    fun friendshipAction(@FieldMap params: Map<String, String>): Observable<ResponseFriendship>


    @FormUrlEncoded
    @POST(FIND_USER)
    fun findUser(@FieldMap params: Map<String, String>):Observable<ResponseFriends>


    @FormUrlEncoded
    @POST(GET_MESSAGES)
    fun getMessages(@FieldMap params: Map<String, String>):Call<ResponseMessage>

    @Multipart
    @POST(SEND_MESSAGE)
    fun sendMessage(@PartMap params: MutableMap<String, RequestBody>, @Part file: MultipartBody.Part? ):Call<ResponseMessage>



    @FormUrlEncoded
    @POST(SEND_MESSAGE)
    fun sendMessage(@FieldMap params: Map<String, String>):Call<ResponseMessage>
    @FormUrlEncoded
    @POST(LAST_MESSAGE_CONTACT)
    fun getLastContactMessage(@FieldMap params: Map<String, String>):Call<ResponseLastMessage>

    //@FormUrlEncoded
    @POST(GET_FOOTBALL_LEAGUES)
    fun getFootballLeagues():Observable<ResponseFootballLeagues>

    @FormUrlEncoded
    @POST(GET_FOOTBALL_DIVISIONS)
    fun getFootballDivisions(@FieldMap params: Map<String, String>):Observable<ResponseFootballDivisions>

    @FormUrlEncoded
    @POST(GET_FOOTBALL_TEAMS)
    fun getFootballTeams(@FieldMap params: Map<String, String>):Observable<ResponseFootballTeams>

    @FormUrlEncoded
    @POST(CREATE_TEAM)
    fun createTeam(@FieldMap params: Map<String, String>): Observable<BaseResponse>

}