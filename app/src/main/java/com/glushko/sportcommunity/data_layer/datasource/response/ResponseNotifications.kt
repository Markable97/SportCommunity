package com.glushko.sportcommunity.data_layer.datasource.response

import com.glushko.sportcommunity.business_logic_layer.domain.Notification

class ResponseNotifications(success: Int,
                            message: String,
                            val notifications: MutableList<Notification> = mutableListOf()
): BaseResponse(success, message)