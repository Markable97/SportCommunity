package com.glushko.sportcommunity.data_layer.datasource.response

class ResponseFriendship(
    success: Int,
    message: String,
    var friend_id: Long
) : BaseResponse(success, message)