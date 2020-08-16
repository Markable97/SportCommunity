package com.glushko.sportcommunity.presentation_layer.ui.team.squad


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Squad

class CompareUserDialog(val squadList: MutableList<Squad.Params>) :  DialogFragment()  {

    companion object{

        private const val KEY_SQUAD = "KEY_SQUAD"

        fun newInstance(squadList: MutableList<Squad.Params>): CompareUserDialog{
            val fragment: CompareUserDialog = CompareUserDialog(squadList)
            return fragment
        }
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
        val listUsers = squadList.filter {
            it.in_app == 1
        }
        val adapterUsers = CompareAdapter(type_item = 0, list = listUsers.toMutableList(), callback = object : CompareAdapter.Callback{
            override fun onItemClick(type_item: Int, item: Squad.Params) {
                TODO("Not yet implemented")
            }
        })
        val recyclerUsers = view.findViewById<RecyclerView>(R.id.user_recycler)
        recyclerUsers.adapter = adapterUsers
        recyclerUsers.layoutManager = LinearLayoutManager(activity)

        //players recycler
        val listPlayers = squadList.filter {
            it.in_app == 0
        }
        val adapterPlayers = CompareAdapter(type_item = 1, list = listPlayers.toMutableList(),callback = object : CompareAdapter.Callback{
            override fun onItemClick(type_item: Int, item: Squad.Params) {
                TODO("Not yet implemented")
            }
        })
        val recyclerPlayers = view.findViewById<RecyclerView>(R.id.player_recycler)
        recyclerPlayers.adapter = adapterPlayers
        recyclerPlayers.layoutManager = LinearLayoutManager(activity)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}