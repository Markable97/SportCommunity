package com.glushko.sportcommunity.business_logic_layer.domain

class TournamentTableFootball {
    data class Params(
        val id_season:Int,
        val  id_league: Int,
        val league_name: String,
        val id_division: Int,
        val division_name: String,
        val id_team: Long,
        val team_name: String,
        val games: Int,
        val wins: Int,
        val draws: Int,
        val losses: Int,
        val sc_con: Int,
        val points: Int
    )
}