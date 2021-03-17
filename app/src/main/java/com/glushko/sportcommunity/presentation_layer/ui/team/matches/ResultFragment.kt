package com.glushko.sportcommunity.presentation_layer.ui.team.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R

class ResultFragment: Fragment() {

    val layoutId: Int = R.layout.fragment_match_result

    companion object{
        fun newInstance():ResultFragment {
            return ResultFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }
}