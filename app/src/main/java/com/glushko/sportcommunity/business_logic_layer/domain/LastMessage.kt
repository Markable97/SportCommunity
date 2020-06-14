package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glushko.sportcommunity.data_layer.datasource.ApiService

class LastMessage {

    @Entity(tableName = "last_messages_table")
    data class Params(
        var message_id: Long,
        @PrimaryKey var contact_id: Long,
        var message_type: Int,
        var contact_name: String,
        var sender_id: Long,
        var receiver_id: Long,
        var message: String = "",
        var message_date: Long = 0,
        var count_notification: Int?
    )

    companion object {
        fun createMap(user_id: Long, token: String): Map<String, String> {
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TOKEN] = token

            return map
        }
    }
}