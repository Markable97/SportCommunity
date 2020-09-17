package com.glushko.sportcommunity.presentation_layer.ui.team.squad


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Squad
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel
import kotlinx.android.synthetic.main.dialog_compare_user.*

class CompareUserDialog(var squadList: MutableList<Squad.Params>, val team_id: Int) :  DialogFragment()  {

    companion object{

        private const val KEY_SQUAD = "KEY_SQUAD"

        fun newInstance(squadList: MutableList<Squad.Params>, team_id: Int): CompareUserDialog{
            return CompareUserDialog(squadList, team_id)
        }
    }

    lateinit var modelSquad: SquadViewModel
    var userList: MutableList<Squad.Params> = mutableListOf()
    var userLinked: Squad.Params? = null
    var userPosition: Int? = null
    var playerList: MutableList<Squad.Params> = mutableListOf()
    var playerLinked: Squad.Params? = null
    var playerPosition: Int? = null
    lateinit var adapterUsers: CompareAdapter
    lateinit var adapterPlayers: CompareAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelSquad = ViewModelProviders.of(this).get(SquadViewModel::class.java)
        modelSquad.liveDataCompare.observe(this, Observer {
            if(it.success == 1){
                changeMainSquadLists()
                userLinked = null
                playerLinked = null
                tvAmpluaLinked.visibility = View.GONE
                tvUserNameLinked.text = "Выберите пользователя "
                tvPlayerLinked.text = "Выберите игрока"
            }else{
                Toast.makeText(activity,"Сопоставление не получилось. Попробуйте позже :(", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_compare_user, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle("Сопоставление игроков")
        userList = squadList.filter {
            it.in_app == 1 && it.linked == 0L
        }.toMutableList()
        adapterUsers = CompareAdapter(type_item = 0, list = userList, callback = object : CompareAdapter.Callback{
            override fun onItemClick(item: Squad.Params, position: Int) {
                tvUserNameLinked.text = item.user_name
                userLinked = item
                userPosition = position
            }
        })
        val recyclerUsers = view.findViewById<RecyclerView>(R.id.user_recycler)
        recyclerUsers.adapter = adapterUsers
        recyclerUsers.layoutManager = LinearLayoutManager(activity)

        //players recycler
        playerList = squadList.filter {
            it.in_app == 0
        }.toMutableList()
        adapterPlayers = CompareAdapter(type_item = 1, list = playerList.toMutableList(),callback = object : CompareAdapter.Callback{
            override fun onItemClick(item: Squad.Params, position: Int) {
                playerLinked = item
                playerPosition = position
                tvPlayerLinked.text = item.player_name
                tvAmpluaLinked.visibility = View.VISIBLE
                tvAmpluaLinked.text = item.amplua
            }
        })
        val recyclerPlayers = view.findViewById<RecyclerView>(R.id.player_recycler)
        recyclerPlayers.adapter = adapterPlayers
        recyclerPlayers.layoutManager = LinearLayoutManager(activity)

        btnLinkedUser.setOnClickListener {
            if(userLinked == null || playerLinked == null){
                Toast.makeText(activity,"Выберите полное сопоставление", Toast.LENGTH_SHORT).show()
            }else{
                modelSquad.compareUsers(1,team_id, userLinked!!.id_user, playerLinked!!.linked)

            }
        }
    }

    private fun changeMainSquadLists(){

        userList.remove(userLinked)
        adapterUsers.setList(userList)

        playerList.remove(playerLinked)
        adapterPlayers.setList(playerList)


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        println("OnDestroy")
    }
}