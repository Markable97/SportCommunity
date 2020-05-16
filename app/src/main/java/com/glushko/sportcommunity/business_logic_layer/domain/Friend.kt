package com.glushko.sportcommunity.business_logic_layer.domain

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Friend {

    data class Params(
        val friend_id: Int,
        val friend_name: String,
        val friend_status: String
    )

    companion object{
        fun createMap(user_id: Int):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.USER_ID] = user_id.toString()
            return map
        }
    }
}