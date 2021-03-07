package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.data_layer.datasource.ApiService

open class BaseResponse (
    var success: Int,
    var message: String){
    companion object{
        fun createMap(user_id: Int, team_id: String):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TEAM_ID] = team_id
            return map
        }

        fun createMap(user_id: Long, team_id: Int, team_name: String):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_TEAM_NAME] = team_name
            return map
        }
        fun createMap(type_compare: Int, user_id: Long, player_id: Long, team_id: Int): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_PLAYER_ID] = player_id.toString()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_TYPE_COMPARE] = type_compare.toString()
            return map
        }
        fun createMap(user_id: Long, team_id: Int, team_name: String, type_invitation: String, token: String):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_TEAM_NAME] = team_name
            map[ApiService.PARAM_TYPE_INVITATION] = type_invitation
            map[ApiService.PARAM_TOKEN] = token
            return map
        }
    }
}