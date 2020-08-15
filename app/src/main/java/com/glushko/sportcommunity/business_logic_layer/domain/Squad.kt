package com.glushko.sportcommunity.business_logic_layer.domain

class Squad {
    data class Params(val id_user: Long,
                      val user_name: String?,
                      val  player_name: String?,
                      val linked: Long,
                      val in_app: Int,
                      val status_invite: String?,
                      val amplua: String?,
                      val status_friend: String?)
}