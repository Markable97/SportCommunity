package com.glushko.sportcommunity.presentation_layer.ui.team.squad

import android.animation.Animator
import android.app.Activity
import android.content.Intent
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
import com.glushko.sportcommunity.presentation_layer.ui.team.events.CreateEventDialog
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import kotlinx.android.synthetic.main.fragment_team_squad.*
import kotlinx.android.synthetic.main.fragment_team_squad_content_main.*

class SquadFragment(private val leader_id: Long, private val team_id: Int, private val team_name: String, private val isLeader: Boolean, val callback: Callback): Fragment() {

    val layoutId: Int = R.layout.fragment_team_squad

    lateinit var modelSquad: SquadViewModel

    var adapter: SquadListAdapter? = null

    var squadList: MutableList<Squad.Params> = mutableListOf()

    var positionDeleteCompare: Int? = null

    var type_compare: Int? = null

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
            println("Live data 2 unknown source")
            squadList = it
            adapter?.setList(squadList)
        })

        modelSquad.liveDataBaseResponse.observe(this, Observer {
            println("Live date compare ${it.message}")
            if(it.success == 1){
                if(type_compare == 2){
                    deleteLocalCompare()
                }else{
                    if(squadList[positionDeleteCompare!!].linked > 0){
                        deleteLocalCompare(false)
                    }
                    squadList.removeAt(positionDeleteCompare!!)
                    adapter?.setList(squadList)
                }
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                adapter?.notifyDataSetChanged()
            }

        })
    }

    private fun deleteLocalCompare(isUpdate: Boolean = true) {
        //Добавляем игркоа
        squadList.add(Squad.Params(id_user = 0,
                                   user_name = null,
                                   player_name = squadList[positionDeleteCompare!!].player_name,
                                   linked = squadList[positionDeleteCompare!!].linked,
                                   in_app = 0,
                                   status_invite = null,
                                   amplua = squadList[positionDeleteCompare!!].amplua,
                                   status_friend = null))
        //для юзера удаляем атрибуты
        squadList[positionDeleteCompare!!].player_name = null
        squadList[positionDeleteCompare!!].linked = 0
        squadList[positionDeleteCompare!!].amplua = null
        if(isUpdate){
            adapter?.setList(squadList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modelSquad.getSquadList(team_id)

        adapter = SquadListAdapter(callback = object : SquadListAdapter.Callback{
            override fun onItemPlayer(
                inApp: Boolean,
                user_id: Long,
                user_name: String?,
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

        squad_team_recycler.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedRight(position: Int) {
                if(isLeader){
                    if(squadList[position].in_app == 1){
                        //Если игрок с app, то сначала отвезать, а потом удалить
                        positionDeleteCompare = position
                        type_compare = 3
                        modelSquad.compareUsers(type_compare!!, team_id, squadList[position].id_user, squadList[position].linked)

                    }else{
                        Toast.makeText(activity, "Нельзя удалить игрока, можно толкьо пользователя", Toast.LENGTH_SHORT).show()
                        adapter?.notifyDataSetChanged()
                    }


                    //adapter?.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(), "У Вас недостаточно прав", Toast.LENGTH_SHORT).show()
                    adapter?.notifyDataSetChanged()
                }

            }

            override fun onSwipedLeft(position: Int) {
                if(isLeader){
                    //modelSquad.compareUsers(2, team_id, squadList[position].id_user, squadList[position].linked)
                    if(squadList[position].in_app > 0 && squadList[position].linked > 0){
                        //Можно отвязать только тех кто привязан и юзер
                        positionDeleteCompare = position
                        type_compare = 2
                        modelSquad.compareUsers(type_compare!!, team_id, squadList[position].id_user, squadList[position].linked)
                    }else{
                        Toast.makeText(activity, "Юзер не привязан", Toast.LENGTH_SHORT).show()
                        adapter?.notifyDataSetChanged()
                    }
                    //adapter?.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(), "У Вас недостаточно прав", Toast.LENGTH_SHORT).show()
                    adapter?.notifyDataSetChanged()
                }
            }

        })
        if(isLeader){
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
                dialogCompare.show(manager, CompareUserDialog.TAG)
            }

            fabLayout_add_player.setOnClickListener {
                val dialogFind = FindUserForSQuadDialog(team_id, team_name)
                val manager = childFragmentManager
                dialogFind.show(manager, "dialogFind")
            }
            fabLayout_add_admin.setOnClickListener{
                val list = squadList.filter{ it.in_app == 1 && it.id_user != leader_id && it.status_invite != "admin" }
                if(list.isNotEmpty()){
                    val dialogSetAdmin = SetAdminDialog.newInstance(team_id.toLong(), team_name, list)
                    dialogSetAdmin.setTargetFragment(this@SquadFragment, SetAdminDialog.TAG_INT)
                    dialogSetAdmin.show(parentFragmentManager, SetAdminDialog.TAG)
                }else{
                    Toast.makeText(requireContext(), "Нет пользователей для назначения", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            fab.visibility = View.GONE
        }
    }

    private fun showFABMenu() {
        fabLayout_compare.visibility = View.VISIBLE
        fabLayout_add_player.visibility = View.VISIBLE
        fabLayout_add_admin.visibility = View.VISIBLE
        fabBGLayout.visibility = View.VISIBLE
        fab.animate().rotationBy(180F)
        fabLayout_compare.animate().translationY(-resources.getDimension(R.dimen.standard_75))
        fabLayout_add_player.animate().translationY(-resources.getDimension(R.dimen.standard_120))
        fabLayout_add_admin.animate().translationY(-resources.getDimension(R.dimen.standard_165))
    }

    private fun closeFABMenu() {
        fabBGLayout.visibility = View.GONE
        fab.animate().rotation(0F)
        fabLayout_add_admin.animate().translationY(0F)
        fabLayout_compare.animate().translationY(0f)
        fabLayout_add_player.animate().translationY(0f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    if (View.GONE == fabBGLayout.visibility) {
                        fabLayout_compare.visibility = View.GONE
                        fabLayout_add_player.visibility = View.GONE
                        fabLayout_add_admin.visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("Events Fragment: onActivityResult")
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                SetAdminDialog.TAG_INT ->{
                    val result = data?.getStringExtra(SetAdminDialog.TAG)
                    if(result == "UPDATE"){
                        val id = data.getLongExtra(SetAdminDialog.KEY1, 0)
                        if(id != 0L){
                            val position = squadList.indexOf( squadList.find {
                                it.id_user == id
                            })
                            squadList[position].status_invite = "admin"
                        }
                    }
                }
            }
        }
    }

    interface Callback{
        fun onClickPlayerInApp(user_id: Long, user_name: String?, status_friend: String?)
    }
}