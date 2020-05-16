package com.glushko.sportcommunity.business_logic_layer.domain

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Register {

    data class Params( var email: String,
                       var name: String,
                       var password: String,
                       var token: String,
                       var idUser: Int = 0)

    companion object {
        fun createRegisterMap(
            email: String,
            name: String,
            password: String,
            token: String
        ): Map<String, String> {
            val map = HashMap<String, String>()
            map.put(ApiService.PARAM_EMAIL, email)
            map.put(ApiService.PARAM_NAME, name)
            map.put(ApiService.PARAM_PASSWORD, password)
            map.put(ApiService.PARAM_TOKEN, token)
            return map
        }
    }
 }