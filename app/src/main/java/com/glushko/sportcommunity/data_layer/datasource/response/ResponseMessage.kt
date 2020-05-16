package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.Message

class ResponseMessage(
    success: Int,
    message: String,
    var messages: MutableList<Message.Params> = mutableListOf()
): BaseResponse(success, message)