package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class ResponseFootballDivisions(success: Int,
                                message: String,
                                val football_divisions: MutableList<Params> = mutableListOf()):BaseResponse(success, message) {

    data class Params(val division_id: Int,
                      val division_name: String){
        override fun toString(): String {
            return division_name
        }
    }

    companion object{
        fun createMap(league_id: Int):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_FOOTBALL_LEAGUE_ID] = league_id.toString()
            return map
        }
    }
}