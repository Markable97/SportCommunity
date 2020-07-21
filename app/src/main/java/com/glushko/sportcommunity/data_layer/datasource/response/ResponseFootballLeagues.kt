package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class ResponseFootballLeagues(success: Int,
                              message: String,
                              val football_leagues: MutableList<Params> = mutableListOf()):BaseResponse(success, message) {

    data class Params(val league_id: Int, val league_name: String){
        override fun toString(): String {
            return league_name
        }
    }


}