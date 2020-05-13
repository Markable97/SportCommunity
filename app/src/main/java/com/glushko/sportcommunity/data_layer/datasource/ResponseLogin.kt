package com.glushko.sportcommunity.data_layer.datasource

class ResponseLogin (
    success: Int,
    message: String,
    val nameUser: String,
    val idUser: Int
): BaseResponse(success, message)