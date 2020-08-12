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
    }
}