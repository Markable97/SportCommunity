package com.glushko.sportcommunity.business_logic_layer.domain

import com.glushko.sportcommunity.data_layer.datasource.ApiService

class Login {

    data class Params(val email: String, val password: String){
        var token: String = ""
    }

    companion object {
        fun createLoginMap(
            email: String,
            password: String,
            token: String
        ): Map<String, String> {
            val map = HashMap<String, String>()
            map.put(ApiService.PARAM_EMAIL, email)
            map.put(ApiService.PARAM_PASSWORD, password)
            map.put(ApiService.PARAM_TOKEN, token)
            return map
        }
    }
}