package com.glushko.sportcommunity.business_logic_layer.domain

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class TeamsUserInfo {


    data class Params(val team_name: String = "",
                      val amplua: String = "",
        val team_id: Int = 0,
        val games: Int = 0,
        val goals: Int = 0,
        val assists: Int = 0,
        val yellow: Int  = 0,
        val red: Int = 0
    )

    companion object{
        fun createMap(user_id: Int):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.USER_ID] = user_id.toString()
            return map
        }
    }
}