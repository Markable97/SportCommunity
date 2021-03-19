package com.glushko.sportcommunity.presentation_layer.ui.team.matches

import com.glushko.sportcommunity.business_logic_layer.domain.MatchFootball

interface UpdateFragmentInterface {
    fun update(matches: List<MatchFootball>)
}