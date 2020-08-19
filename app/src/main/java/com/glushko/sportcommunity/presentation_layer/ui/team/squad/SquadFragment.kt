package com.glushko.sportcommunity.presentation_layer.ui.team.squad

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Squad
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel
import kotlinx.android.synthetic.main.fragment_team_squad.*
import kotlinx.android.synthetic.main.fragment_team_squad_content_main.*

class SquadFragment(private val team_id: Int, private val team_name: String, val callback: Callback): Fragment() {

    val layoutId: Int = R.layout.fragment_team_squad

    lateinit var modelSquad: SquadViewModel

    var adapter: SquadListAdapter? = null

    var squadList: MutableList<Squad.Params> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelSquad = ViewModelProviders.of(this).get(SquadViewModel::class.java)

        modelSquad.liveDataSquadListResponse.observe(this, Observer {
            println("Live data squad list ${it.success} ${it.message}")
            if(it.success == 1){
                squadList = it.squad
                adapter?.setList(it.squad)
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        modelSquad.liveDataSquadList.observe(this, Observer {
            squadList = it
            adapter?.setList(squadList)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modelSquad.getSquadList(team_id)

        adapter = SquadListAdapter(callback = object : SquadListAdapter.Callback{
            override fun onItemPlayer(
                inApp: Boolean,
                user_id: Long,
                user_name: String,
                status_friend: String?
            ) {
                if(inApp){
                    callback.onClickPlayerInApp(user_id, user_name, status_friend)
                }else{
                    Toast.makeText(activity, "Player in app $inApp", Toast.LENGTH_SHORT).show()
                }
            }

        })
        squad_team_recycler.adapter = adapter
        squad_team_recycler.layoutManager = LinearLayoutManager(activity)

        fab.setOnClickListener {
            if (View.GONE == fabBGLayout.visibility) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        fabBGLayout.setOnClickListener { closeFABMenu() }

        fabLayout_compare.setOnClickListener {
            val dialogCompare = CompareUserDialog.newInstance(squadList, team_id)
            val manager = childFragmentManager
            dialogCompare.show(manager, "dialogCompare")
        }

        fabLayout_add_player.setOnClickListener {
            val dialogFind = FindUserForSQuadDialog(team_id, team_name)
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

    interface Callback{
        fun onClickPlayerInApp(user_id: Long, user_name: String, status_friend: String?)
    }
}