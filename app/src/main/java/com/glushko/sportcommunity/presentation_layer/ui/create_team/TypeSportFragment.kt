package com.glushko.sportcommunity.presentation_layer.ui.create_team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R
import com.realpacific.clickshrinkeffect.applyClickShrink
import kotlinx.android.synthetic.main.fragment_create_team_type_sport.*

class TypeSportFragment : Fragment() {

    val layoutId: Int = R.layout.fragment_create_team_type_sport

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFootball.applyClickShrink()
        btnFootball.setOnClickListener {

        }
        btnBasketball.applyClickShrink()
        btnBasketball.setOnClickListener {

        }
        btnHockey.applyClickShrink()
        btnHockey.setOnClickListener {

        }
        btnVolleyball.applyClickShrink()
        btnVolleyball.setOnClickListener {

        }
        btnOtherTypeSport.applyClickShrink()
        btnOtherTypeSport.setOnClickListener {

        }
    }
}