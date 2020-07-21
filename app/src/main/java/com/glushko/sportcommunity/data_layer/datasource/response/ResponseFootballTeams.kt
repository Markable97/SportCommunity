package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class ResponseFootballTeams(success: Int,
                            message: String,
                             val football_teams: MutableList<Params> = mutableListOf()):BaseResponse(success, message) {

    data class Params(val team_id: Int,
                      val team_name: String){
        override fun toString(): String {
            return team_name
        }
    }
    companion object{
        fun createMap(division_id: Int):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_FOOTBALL_DIVISION_ID] = division_id.toString()
            return map
        }
    }
}