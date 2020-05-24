package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage

class ResponseLastMessage(
    success: Int,
    message: String,
    var lastMessages: MutableList<LastMessage.Params> = mutableListOf()
):BaseResponse(success, message)