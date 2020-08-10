package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Friend {
    @Entity(tableName = "friends_table")
    data class Params(
        @PrimaryKey()
        val friend_id: Int,
        val user_name: String,
        val user_status: String,
        var status_friend: String?,
        var team_id: Int,
        var status_in_team: String?
    )

    companion object{
        fun createMap(user_id: Int):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            return map
        }

        fun createMap(user_name: String, user_id: Long, type_find: String):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_NAME] = user_name
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TYPE_FIND_USER] = type_find
            return map
        }

        fun createMap(user_name: String, user_id: Long, type_find: String, team_id: Int):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_NAME] = user_name
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TYPE_FIND_USER] = type_find
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            return map
        }

        fun createMap(user_id: Long, user_name:String, friend_id: Long, action: String, token: String): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_USER_NAME] = user_name
            map[ApiService.PARAM_FRIEND_ID] = friend_id.toString()
            map[ApiService.PARAM_FRIENDSHIP_ACTION] = action
            map[ApiService.PARAM_TOKEN] = token
            return map
        }
    }
}