package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.MatchFootball
import com.glushko.sportcommunity.data_layer.datasource.ApiService

class ResponseFootballMatches( success: Int,
                               message: String,
                               val matches: MutableList<MatchFootball> = mutableListOf()): BaseResponse(success, message) {
    companion object{
        fun createMap(team_id: Long):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            return map
        }
    }
}