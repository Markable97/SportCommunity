package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Notification {
    @Entity(tableName = "notifications")
    data class Params(
        @PrimaryKey
        val notification_id: Long,
        val notification_type: String, /*invitation in team or event*/
        val team_id: Int,
        val team_name: String,
        val event_name: String?
    )

    companion object{
        fun createMap(user_id: Long, token: String): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TOKEN] = token
            return map
        }
    }
}