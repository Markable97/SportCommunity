package com.glushko.sportcommunity.data_layer.datasource

import com.glushko.sportcommunity.business_logic_layer.domain.TeamsUserInfo

class ResponseMainPage(
    success: Int,
    message: String,
    var teamsUserinfo: MutableList<TeamsUserInfo.Params> = mutableListOf<TeamsUserInfo.Params>()
): BaseResponse(success, message)

