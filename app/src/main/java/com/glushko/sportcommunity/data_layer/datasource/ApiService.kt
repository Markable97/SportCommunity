package com.glushko.sportcommunity.data_layer.datasource

import com.glushko.sportcommunity.data_layer.datasource.response.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFriends
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseLogin
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMainPage
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded

interface ApiService {
    companion object{ //Что то типо статических вспомогательных классов
        //Methods
        const val REGISTER = "Register"
        const val LOGIN = "Login"
        const val MAIN_PAGE = "UserMainPage"
        const val FRIENDS = "GetFriends"
        //params
        const val PARAM_EMAIL = "email"
        const val PARAM_PASSWORD = "password"
        const val PARAM_NAME = "name"
        const val PARAM_TOKEN = "token"
        const val PARAM_USER_DATE = "user_date"
        const val USER_ID = "user_id"
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
}