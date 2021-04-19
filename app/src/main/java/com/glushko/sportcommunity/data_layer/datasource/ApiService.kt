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
        const val GET_TEAM_INFO = "GetTeamInformation"
        const val GET_FRIENDS = "GetFriends"
        const val GET_MESSAGES = "GetMessages"
        const val GET_MESSAGES_TEAM = "GetMessagesTeam"
        const val SEND_MESSAGE = "SendMessage"
        const val SEND_MESSAGE_TEAM = "SendMessageTeam"
        const val LAST_MESSAGE_CONTACT = "GetLastContactMessage"
        const val FIND_USER = "FindPeople"
        const val FRIENDSHIP = "Friendship"
        const val GET_FOOTBALL_LEAGUES = "GetFootballLeague"
        const val GET_FOOTBALL_DIVISIONS = "GetFootballDivisions"
        const val GET_FOOTBALL_TEAMS = "GetFootballTeams"
        const val GET_TOURNAMENT_TABLE_FOOTBALL = "GetTournamentTableFootball"
        const val GET_FOOTBALL_MATCHES_TEAM = "GetFootballMatchesTeam"
        const val CREATE_TEAM = "CreateTeam"
        const val INVITE_IN_TEAM = "InviteInTeam"
        const val GET_SQUAD_TEAM_LIST = "SquadTeamList"
        const val COMPARE_USERS = "CompareUsers"
        const val GET_NOTIFICATIONS = "GetNotifications"
        const val GET_EVENTS_TEAM = "GetEventsTeam"
        const val CREATE_EVENT = "CreateEvent"
        const val DELETE_EVENT = "DeleteEvent"
        const val MODIFY_CHOICE_EVENT = "ModifyChoiceEvent"
        const val GET_PLAYERS_IN_MATCH = "GetPlayersInMatch"

        //params
        const val PARAM_EMAIL = "email"
        const val PARAM_PASSWORD = "password"
        const val PARAM_NAME = "name"
        const val PARAM_TOKEN = "token"
        const val PARAM_MESSAGE_TYPE = "type_message"
        const val PARAM_MESSAGE_DATE = "user_date"
        const val PARAM_USER_ID = "user_id"
        const val PARAM_PLAYER_ID = "player_id"
        const val PARAM_FRIEND_ID = "friend_id"
        const val PARAM_SENDER_ID = "sender_id"
        const val PARAM_RECEIVER_ID = "receiver_id"
        const val PARAM_MESSAGE = "message"
        const val PARAM_USER_NAME = "user_name"
        const val PARAM_FRIENDSHIP_ACTION = "action"
        const val PARAM_FOOTBALL_LEAGUE_ID = "league_id"
        const val PARAM_FOOTBALL_DIVISION_ID = "division_id"
        const val PARAM_FOOTBALL_SEASON_ID = "season_id"
        const val PARAM_TEAM_ID = "team_id"
        const val PARAM_TEAM_NAME = "team_name"
        const val PARAM_TYPE_FIND_USER = "type_find"
        const val PARAM_TYPE_COMPARE = "type_compare"
        const val PARAM_TYPE_INVITATION = "type_invitation"
        const val PARAM_EVENT_ID = "event_id"
        const val PARAM_EVENT_NAME = "event_name"
        const val PARAM_EVENT_DATE = "event_date"
        const val PARAM_EVENT_LOCATION = "event_location"
        const val PARAM_EVENT_POSITIVE_NAME = "positive_name"
        const val PARAM_EVENT_NEGATIVE_NAME = "negative_name"
        const val PARAM_EVENT_NEUTRAL_NAME = "neutral_name"
        const val PARAM_CHOICE_MODE_EVENT = "choice_mode_event"
        const val PARAM_EVENT_CHOICE = "choice"
        const val PARAM_MATCH_ID = "match_id"
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
    @POST(GET_TEAM_INFO)
    fun getTeamInfo(@FieldMap params: Map<String, String>): Observable<ResponseMainPage>

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

    @FormUrlEncoded
    @POST(GET_MESSAGES_TEAM)
    fun getMessagesTeam(@FieldMap params: Map<String, String>):Call<ResponseMessage>

    @Multipart
    @POST(SEND_MESSAGE)
    fun sendMessage(@PartMap params: MutableMap<String, RequestBody>, @Part file: MultipartBody.Part? ):Call<ResponseMessage>

    @Multipart
    @POST(SEND_MESSAGE_TEAM)
    fun sendMessageTeam(@PartMap params: MutableMap<String, RequestBody>, @Part file: MultipartBody.Part? ):Call<ResponseMessage>

    @FormUrlEncoded
    @POST(SEND_MESSAGE)
    fun sendMessage(@FieldMap params: Map<String, String>):Call<ResponseMessage>
    @FormUrlEncoded
    @POST(SEND_MESSAGE_TEAM)
    fun sendMessageTeam(@FieldMap params: Map<String, String>):Call<ResponseMessage>
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
    @POST(GET_TOURNAMENT_TABLE_FOOTBALL)
    fun getTournamentTableFootball(@FieldMap params: Map<String, String>): Observable<ResponseTournamentTableFootball>

    @FormUrlEncoded
    @POST(GET_FOOTBALL_MATCHES_TEAM)
    fun getFootballMatchesTeam(@FieldMap params: Map<String, String>): Observable<ResponseFootballMatches>

    @FormUrlEncoded
    @POST(CREATE_TEAM)
    fun createTeam(@FieldMap params: Map<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(INVITE_IN_TEAM)
    fun inviteInTeam(@FieldMap params: Map<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(GET_SQUAD_TEAM_LIST)
    fun getSquadTeamList(@FieldMap param: Map<String, String>): Observable<ResponseSquadTeamList>

    @FormUrlEncoded
    @POST(COMPARE_USERS)
    fun compareUsers(@FieldMap param: Map<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(GET_NOTIFICATIONS)
    fun getNotifications(@FieldMap param: Map<String, String>): Observable<ResponseNotifications>

    @FormUrlEncoded
    @POST(GET_EVENTS_TEAM)
    fun getEventsTeam(@FieldMap param: Map<String, String>): Observable<ResponseEventsTeam>

    @FormUrlEncoded
    @POST(CREATE_EVENT)
    fun createEvent(@FieldMap param: Map<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(DELETE_EVENT)
    fun deleteEvent(@FieldMap param: Map<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(MODIFY_CHOICE_EVENT)
    fun modifyChoiceEvent(@FieldMap param: Map<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST(GET_PLAYERS_IN_MATCH)
    fun getPlayersInMatch(@FieldMap param: Map<String, String>): Observable<ResponsePlayersInMatch>

}