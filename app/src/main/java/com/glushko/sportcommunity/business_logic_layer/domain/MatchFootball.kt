package com.glushko.sportcommunity.business_logic_layer.domain

data class MatchFootball(
    val season_id: Int,
    val league_name: String,
    val division_id: Int,
    val division_name: String,
    val match_id: Long,
    val tour_id: Int,
    val team_home_id: Int,
    val team_home_name: String,
    val goal_home:Int,
    val goal_guest: Int,
    val team_guest_id: Int,
    val team_guest_name: String,
    val match_date: String?,
    val name_stadium: String?,
    val match_desc: String?,
    val played: Int
)