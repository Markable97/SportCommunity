package com.glushko.sportcommunity.data_layer.datasource

import com.glushko.sportcommunity.data_layer.datasource.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object{ //Что то типо статических вспомогательных классов
        //Methods
        const val REGISTER = "Register"
        const val LOGIN = "Login"
        const val MAIN_PAGE = "UserMainPage"
        const val FRIENDS = "GetFriends"
        const val GET_MESSAGES = "GetMessages"
        const val SEND_MESSAGE = "SendMessage"
        //params
        const val PARAM_EMAIL = "email"
        const val PARAM_PASSWORD = "password"
        const val PARAM_NAME = "name"
        const val PARAM_TOKEN = "token"
        const val PARAM_USER_DATE = "user_date"
        const val PARAM_USER_ID = "user_id"
        const val PARAM_SENDER_ID = "sender_id"
        const val PARAM_RECEIVER_ID = "receiver_id"
        const val PARAM_MESSAGE = "message"
    }

    @FormUrlEncoded
    @POST(REGISTER)
    fun register(@FieldMap params: Map<String, String>) : Call<BaseResponse>

    @FormUrlEncoded
    @POST(LOGIN)
    fun login(@FieldMap params: Map<String, String>) : Call<ResponseLogin>

    @FormUrlEncoded
    @POST(MAIN_PAGE)
    fun main_page(@FieldMap params: Map<String, String>):Call<ResponseMainPage>

    @FormUrlEncoded
    @POST(FRIENDS)
    fun getFriends(@FieldMap params: Map<String, String>):Call<ResponseFriends>

    @FormUrlEncoded
    @POST(GET_MESSAGES)
    fun getMessages(@FieldMap params: Map<String, String>):Call<ResponseMessage>

    @FormUrlEncoded
    @POST(SEND_MESSAGE)
    fun sendMessage(@FieldMap params: Map<String, String>):Call<ResponseMessage>


}