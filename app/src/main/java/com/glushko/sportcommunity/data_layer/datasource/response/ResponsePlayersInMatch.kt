package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.Player
import com.glushko.sportcommunity.data_layer.datasource.ApiService


class ResponsePlayersInMatch(success: Int,
                             message: String,
                             val players_in_match: MutableList<Player.Params> = mutableListOf()): BaseResponse(success, message)  {
    companion object{
        fun createMap(match_id: Long):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_MATCH_ID] = match_id.toString()
            return map
        }
    }
}