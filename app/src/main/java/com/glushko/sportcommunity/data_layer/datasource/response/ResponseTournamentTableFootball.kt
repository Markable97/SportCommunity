package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.TournamentTableFootball
import com.glushko.sportcommunity.data_layer.datasource.ApiService


class ResponseTournamentTableFootball(success: Int,
                                      message: String,
                                      val tournament_table: MutableList<TournamentTableFootball.Params> = mutableListOf()): BaseResponse(success, message) {
    companion object {
        fun createMap(division_id: Int, season_id: Int, team_id: Long): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_FOOTBALL_DIVISION_ID] = division_id.toString()
            map[ApiService.PARAM_FOOTBALL_SEASON_ID] = season_id.toString()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            return map
        }
    }
}