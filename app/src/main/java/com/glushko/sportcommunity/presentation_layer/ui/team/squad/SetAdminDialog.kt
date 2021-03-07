package com.glushko.sportcommunity.presentation_layer.ui.team.squad

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Squad
import kotlinx.android.synthetic.main.dialog_set_admin.*

class SetAdminDialog : DialogFragment()  {

    companion object{

        const val TAG = "adm_dialog"

        private const val KEY1 = "team_id"
        private const val KEY2 = "team_name"
        private const val KEY3 = "squad_list"

        fun newInstance(team_id: Long, team_name: String, squadList: List<Squad.Params>): SetAdminDialog{
            val bundle = Bundle()
            bundle.putLong(KEY1, team_id)
            bundle.putString(KEY2, team_name)
            val list: Array<Squad.Params> = squadList.toTypedArray()
            bundle.putParcelableArray(KEY3, list)
            val fragment = SetAdminDialog()
            fragment.arguments = bundle
            return  fragment
            /*return SetAdminDialog().apply {

                arguments = Bundle().apply {
                    putLong(KEY1, team_id)
                    putString(KEY2, team_name)
                    val list: Array<Squad.Params> = squadList.toTypedArray()
                    putParcelableArray(KEY3, list)
                }
            }*/
        }
    }

    lateinit var adapter: SquadListAdapter
    private val usersForAdmin: MutableList<Squad.Params> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_set_admin, null)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle("Назначить админа")
        val bundle = arguments
        if (bundle != null){
            usersForAdmin.addAll(bundle.getParcelableArray(KEY3) as Array<Squad.Params>)
        }

        adapter = SquadListAdapter(list = usersForAdmin, callback = object : SquadListAdapter.Callback{
            override fun onItemPlayer(
                inApp: Boolean,
                user_id: Long,
                user_name: String?,
                status_friend: String?
            ) {
                tvUserAdmin.text = user_name
            }

        })

        val recyclerUsers = view.findViewById<RecyclerView>(R.id.users_admin_recycler)
        recyclerUsers.adapter = adapter
        recyclerUsers.layoutManager = LinearLayoutManager(activity)


    }

    //Делает диалоговое окна на весь экран
    override fun onResume() {
        if(dialog != null ){
            if(dialog!!.window != null){
                val window = dialog!!.window
                window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,  WindowManager.LayoutParams.MATCH_PARENT)
            }
        }
        super.onResume()
    }
    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { _activity ->
            val builder = AlertDialog.Builder(_activity)
            val view: View = _activity.layoutInflater.inflate(R.layout.dialog_set_admin, null)

            builder.setView(view)
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }*/
}