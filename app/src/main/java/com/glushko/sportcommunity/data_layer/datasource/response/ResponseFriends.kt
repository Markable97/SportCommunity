package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.Friend

class ResponseFriends(
    success: Int,
    message: String,
    val friends: MutableList<Friend.Params> = mutableListOf()
) : BaseResponse(success, message)