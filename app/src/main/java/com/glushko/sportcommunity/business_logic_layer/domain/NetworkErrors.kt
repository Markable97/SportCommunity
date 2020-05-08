package com.glushko.sportcommunity.business_logic_layer.domain

class NetworkErrors(message: String, cause: Throwable): Throwable(message, cause)