package com.glushko.sportcommunity.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glushko.sportcommunity.data_layer.datasource.ApiService

class TeamsUserInfo {

    @Entity(tableName = "main_page")
    data class Params(val team_name: String = "",
                      val amplua: String = "",
                      @PrimaryKey val team_id: Int = 0,
                      val team_desc: String = "",
                      val leader_id: Int = 0,
                      val leader_name: String = "",
                      val games: Int = 0,
                      val goals: Int = 0,
                      val assists: Int = 0,
                      val yellow: Int  = 0,
                      val red: Int = 0
    )

    companion object{
        fun createMap(user_id: Int):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            return map
        }

        fun createMap(user_id: Long, team_id: Long, token: String): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_TOKEN] = token
            return map
        }
    }
}