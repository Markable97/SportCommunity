package com.glushko.sportcommunity.business_logic_layer.domain

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Event {
    data class Params(
        var user_id: Long,
        var team_id: Long,
        var team_name: String,
        var event_name: String,
        var event_date: String?,
        var event_location: String?,
        var positive_name: String?,
        var positive_count: Int,
        val negative_name: String?,
        var negative_count: Int,
        var neutral_name: String?,
        var neutral_count: Int,
        var event_id: Long,
        var user_choice: String,
        var is_leader: Int
    ) {
        constructor(
            team_id: Long,
            user_id: Long,
            team_name: String,
            event_name: String,
            event_location: String?,
            event_date: String?,
            positive_name: String?,
            negative_name: String?,
            neutral_name: String?
        ) : this(
            user_id,
            team_id,
            team_name,
            event_name,
            event_date,
            event_location,
            positive_name,
            0,
            negative_name,
            0,
            neutral_name,
            0,
            0,
            "",
            0
        )
    }

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

        fun createMap(user_id: Long, token: String, event_id: Long, mode_choice:String, choice:String): Map<String, String>{
            val map = HashMap<String, String>()
            map[ApiService.PARAM_USER_ID] = user_id.toString()
            map[ApiService.PARAM_TOKEN] = token
            map[ApiService.PARAM_EVENT_ID] = event_id.toString()
            map[ApiService.PARAM_CHOICE_MODE_EVENT] = mode_choice
            map[ApiService.PARAM_EVENT_CHOICE] = choice
            return map
        }
    }
}