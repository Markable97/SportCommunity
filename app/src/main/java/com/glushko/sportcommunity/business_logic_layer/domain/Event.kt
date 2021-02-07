package com.glushko.sportcommunity.business_logic_layer.domain

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Event {
    data class Params(
        val user_id: Long,
        val team_id: Long,
        val team_name: String,
        val event_name: String,
        val event_date: String?,
        val event_location: String?,
        val positive_name: String?,
        val positive_count: Int,
        val negative_name: String?,
        val negative_count: Int,
        val neutral_name: String?,
        val neutral_count: Int,
        val event_id: Long
    )

    companion object{
        fun createMap(user_id: Long, team_id: Long, token: String): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_TOKEN] = token
            return map
        }
        fun createMap(token: String, user_id: Long, event_id: Long):Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_TOKEN] = token
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_EVENT_ID] = event_id.toString()
            return map
        }

        fun createMap(user_id: Long, token: String, team_id: Long, team_name: String, event_name: String, event_date: String?, event_location: String?,
                      positive_name: String?, negative_name: String?, neutral_name: String?): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TOKEN] = token
            map[ApiService.PARAM_TEAM_ID] = team_id.toString()
            map[ApiService.PARAM_TEAM_NAME] = team_name
            map[ApiService.PARAM_EVENT_NAME] = event_name
            event_date?.let { map[ApiService.PARAM_EVENT_DATE] = it }
            event_location?.let { map[ApiService.PARAM_EVENT_LOCATION] = it }
            positive_name?.let { map[ApiService.PARAM_EVENT_POSITIVE_NAME] = it }
            negative_name?.let { map[ApiService.PARAM_EVENT_NEGATIVE_NAME] = it }
            neutral_name?.let { map[ApiService.PARAM_EVENT_NEUTRAL_NAME] = it }
            return map
        }
    }
}