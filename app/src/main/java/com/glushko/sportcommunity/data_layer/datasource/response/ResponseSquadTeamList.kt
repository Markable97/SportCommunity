package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.Squad
import com.glushko.sportcommunity.data_layer.datasource.ApiService

class ResponseSquadTeamList(success: Int,
                            message: String,
                            val squad: MutableList<Squad.Params> = mutableListOf()): BaseResponse(success, message) {
    companion object {
        fun createMap(team_id: Int, user_id: Long): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            return map
        }
    }
}