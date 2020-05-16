package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.TeamsUserInfo
import com.glushko.sportcommunity.data_layer.datasource.response.BaseResponse

class ResponseMainPage(
    success: Int,
    message: String,
    var teamsUserinfo: MutableList<TeamsUserInfo.Params> = mutableListOf<TeamsUserInfo.Params>()
): BaseResponse(success, message)

