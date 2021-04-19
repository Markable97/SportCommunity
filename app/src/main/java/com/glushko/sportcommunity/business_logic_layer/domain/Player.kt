package com.glushko.sportcommunity.business_logic_layer.domain

class Player {
    data class Params(
        val player_id: Long,
        val player_name: String,
        val team_name: String,
        val team_id: Long,
        val birthday: String,
        val amplua: String,
        val number: Int,
        val games: Int,
        val goal: Int,
        val penalty: Int,
        val penalty_out: Int,
        val assist: Int,
        val yellow: Int,
        val red: Int,
        val own_goal: Int,
        val in_game: Int
    )
}