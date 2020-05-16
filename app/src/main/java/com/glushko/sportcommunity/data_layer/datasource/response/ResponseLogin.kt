package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.data_layer.datasource.response.BaseResponse

class ResponseLogin (
    success: Int,
    message: String,
    val nameUser: String,
    val idUser: Int
): BaseResponse(success, message)