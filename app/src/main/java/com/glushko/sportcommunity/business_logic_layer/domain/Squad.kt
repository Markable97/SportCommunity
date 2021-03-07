package com.glushko.sportcommunity.business_logic_layer.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Squad {
    @Parcelize
    data class Params(var id_user: Long,
                      var user_name: String?,
                      var  player_name: String?,
                      var linked: Long,
                      var in_app: Int,
                      var status_invite: String?,
                      var amplua: String?,
                      var status_friend: String?): Parcelable
}