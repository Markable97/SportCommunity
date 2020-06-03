package com.glushko.sportcommunity.data_layer.datasource

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {

    const val BASE_URL = "http://89.208.205.160:8080/SportCommunityServer/"

    const val BASE_URL_IMAGE = "http://89.208.205.160:8080/SportCommunityServer/ImagesOfTeams/"

    const val BASE_URL_IMAGE_CHAT = "http://89.208.205.160:8080/SportCommunityServer/ImagesOfChats/"

    //Вовзращает retrofit, из него можно слать запросы к серверу
    fun makeNetworkService(): ApiService{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(client.build())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}