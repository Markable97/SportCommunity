package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Message {
    @Entity(tableName = "messages_table")
    data class Params(
        @PrimaryKey(autoGenerate = true) var message_id: Long,
        var sender_id: Long,
        var receiver_id: Long,
        var message_text: String = "",
        var date: Long = 0
    )

    companion object{
        fun createMap(sender_id: Long, receiver_id: Long, token: String):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_SENDER_ID] = sender_id.toString()
            map[ApiService.PARAM_RECEIVER_ID] = receiver_id.toString()
            map[ApiService.PARAM_TOKEN] = token

            return map
        }
    }
}