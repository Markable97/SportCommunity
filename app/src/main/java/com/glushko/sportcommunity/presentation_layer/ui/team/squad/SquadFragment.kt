package com.glushko.sportcommunity.presentation_layer.ui.team.squad

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R
import kotlinx.android.synthetic.main.fragment_team_squad.*

class SquadFragment(private val team_id: Int): Fragment() {

    val layoutId: Int = R.layout.fragment_team_squad

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            if (View.GONE == fabBGLayout.visibility) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        fabBGLayout.setOnClickListener { closeFABMenu() }

        fabLayout_compare.setOnClickListener {
            Toast.makeText(activity, "Нажата кнопка сопоставления", Toast.LENGTH_SHORT).show()
        }

        fabLayout_add_player.setOnClickListener {
            val dialogFind = FindUserForSQuadDialog(team_id)
            val manager = childFragmentManager
            dialogFind.show(manager, "dialogFind")
        }
    }

    private fun showFABMenu() {
        fabLayout_compare.visibility = View.VISIBLE
        fabLayout_add_player.visibility = View.VISIBLE
        fabBGLayout.visibility = View.VISIBLE
        fab.animate().rotationBy(180F)
        fabLayout_compare.animate().translationY(-resources.getDimension(R.dimen.standard_75))
        fabLayout_add_player.animate().translationY(-resources.getDimension(R.dimen.standard_120))
    }

    private fun closeFABMenu() {
        fabBGLayout.visibility = View.GONE
        fab.animate().rotation(0F)
        fabLayout_compare.animate().translationY(0f)
        fabLayout_add_player.animate().translationY(0f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    if (View.GONE == fabBGLayout.visibility) {
                        fabLayout_compare.visibility = View.GONE
                        fabLayout_add_player.visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

    }

}