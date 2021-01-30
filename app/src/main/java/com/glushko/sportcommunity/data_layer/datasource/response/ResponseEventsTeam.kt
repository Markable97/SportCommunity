package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.Event

class ResponseEventsTeam(success: Int,
                         message: String,
                         val events: MutableList<Event> = mutableListOf()): BaseResponse(success, message)