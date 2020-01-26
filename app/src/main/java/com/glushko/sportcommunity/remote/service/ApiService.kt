package com.glushko.sportcommunity.remote.service

import com.glushko.sportcommunity.remote.core.BaseResponse
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    companion object{
        //methods
        const val REGISTER = "register.php"

        //params
        const val PARAM_EMAIL = "email"
        const val PARAM_PASSWORD = "password"
        const val PARAM_NAME = "name"
        const val PARAM_TOKEN = "token"
        const val PARAM_USER_DATE = "user_date"
    }

    @FormUrlEncoded
    @POST(REGISTER)
    fun register(@FieldMap params: Map<String, String>): Call<BaseResponse>
}

/**
Что?
Интерфейс с функциями API. Содержит: вспомогательные константы с именем метода и параметрами, функцию для регистрации
(fun register(…)).

Зачем?
Для формирования API запросов.

Функции:
fun register(…) формирует API запрос для регистрации. Использует аннотации @FormUrlEncoded и @POST(<Имя метода API>).
Принимает Map<String, String>. Возвращает Call<BaseResponse>.*/