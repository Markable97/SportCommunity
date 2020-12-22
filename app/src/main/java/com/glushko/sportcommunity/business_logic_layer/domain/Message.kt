package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import okhttp3.RequestBody

class Message {
    @Entity(tableName = "messages_table")
    data class Params(
        @PrimaryKey(autoGenerate = true) var message_id: Long,
        var message_type: Int,
        var sender_id: Long,
        var receiver_id: Long,
        var message: String = "",
        var message_date: Long = 0
    )

    //Params(message_id=74, message_type=1, sender_id=42, receiver_id=45, message=��, message_date=1602002564)
    //[Params(message_id=3, message_type=1, sender_id=45, receiver_id=16, message=bla bla, message_date=20201218121424)

    companion object{
        fun createMap(sender_id: Long, receiver_id: Long, token: String):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_SENDER_ID] = sender_id.toString()
            map[ApiService.PARAM_RECEIVER_ID] = receiver_id.toString()
            map[ApiService.PARAM_TOKEN] = token

            return map
        }
        fun createMap(team_id: Long, token: String): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_TOKEN] = token
            return map
        }
        /*@Target(AnnotationTarget.TYPE, AnnotationTarget.EXPRESSION, AnnotationTarget.FUNCTION)
        @Retention(AnnotationRetention.SOURCE)
        annotation class JvmSuppressWildcard
        @JvmSuppressWildcard*/
        fun createMapFile(sender_id: Long, receiver_id: Long, token: String, message: String, message_type: Int):MutableMap<String,  RequestBody>{

            val map = mutableMapOf<String, RequestBody>()

            map[ApiService.PARAM_SENDER_ID] =  RequestBody.create(
                okhttp3.MultipartBody.FORM,  sender_id.toString())
            map[ApiService.PARAM_RECEIVER_ID] =   RequestBody.create(
                okhttp3.MultipartBody.FORM, receiver_id.toString())
            map[ApiService.PARAM_TOKEN] =  RequestBody.create(
                okhttp3.MultipartBody.FORM, token)
            map[ApiService.PARAM_MESSAGE] =  RequestBody.create(
                okhttp3.MultipartBody.FORM, message)
            map[ApiService.PARAM_MESSAGE_TYPE] =   RequestBody.create(
                okhttp3.MultipartBody.FORM, message_type.toString())
            return map
        }

        annotation class JvmSuppressWildcard


        fun createMap(sender_id: Long, receiver_id: Long, token: String, message: String, message_type: Int):Map<String, String> {
            val map = HashMap<String, String>()
            map[ApiService.PARAM_SENDER_ID] = sender_id.toString()
            map[ApiService.PARAM_RECEIVER_ID] = receiver_id.toString()
            map[ApiService.PARAM_TOKEN] = token
            map[ApiService.PARAM_MESSAGE] = message
            map[ApiService.PARAM_MESSAGE_TYPE] = message_type.toString()
            return map
        }
    }
}