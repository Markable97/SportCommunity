package com.glushko.sportcommunity.presentation_layer.ui.create_team

import android.content.Context
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

    lateinit var callback: CallbackTypeSport

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as CallbackTypeSport
    }

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
            callback.onClickTypeSport("Football")
        }
        btnBasketball.applyClickShrink()
        btnBasketball.setOnClickListener {
            callback.onClickTypeSport("Basketball")
        }
        btnHockey.applyClickShrink()
        btnHockey.setOnClickListener {
            callback.onClickTypeSport("Hockey")
        }
        btnVolleyball.applyClickShrink()
        btnVolleyball.setOnClickListener {
            callback.onClickTypeSport("Volleyball")
        }
        btnOtherTypeSport.applyClickShrink()
        btnOtherTypeSport.setOnClickListener {
            callback.onClickTypeSport("Other Sport")
        }
    }

    interface CallbackTypeSport{
        fun onClickTypeSport(typeSport: String)
    }
}