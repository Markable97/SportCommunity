package com.glushko.sportcommunity.presentation_layer.ui.notification

import android.app.Application
import android.content.Context
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
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.vm.FriendsViewModel
import com.glushko.sportcommunity.presentation_layer.vm.NotificationDrawerViewModel
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : Fragment() {

    val layoutId = R.layout.fragment_notification

    lateinit var adapter: NotificationsAdapter

    private lateinit var modelNotification: NotificationDrawerViewModel
    private lateinit var modelFriend: FriendsViewModel
    var idUser: Int? = null
    var positionAdapter: Int? = null
    var notificationId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = SharedPrefsManager(requireActivity().getSharedPreferences(requireActivity().packageName, Context.MODE_PRIVATE))
        idUser = pref.getAccount().idUser
        modelNotification = ViewModelProviders.of(this).get(NotificationDrawerViewModel::class.java)

        modelNotification.notificationsLiveData.observe(this, Observer {
            println("NotificationFragment Live Data Notification  $it")
            adapter.setList(it.toMutableList())
        })

        modelFriend = ViewModelProviders.of(this).get(FriendsViewModel::class.java)
        modelFriend.liveDataInvitationInTeam.observe(this, Observer {
            println("NotificationFragment Live Data invitation $it")
            if(it.success == 1){
                if(positionAdapter!= null && notificationId != null) {
                    adapter.deletePosition(positionAdapter!!)
                    modelNotification.deleteChooseNotification(notificationId!!)
                }

            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotificationsAdapter(callback = object : NotificationsAdapter.Callback{
            override fun onClickBtn(
                join: Boolean,
                position: Int,
                team_id: Int,
                team_name: String,
                notification_id: Long
            ) {
                notificationId = notification_id
                positionAdapter = position
                if(idUser != null){
                    modelFriend.inviteInTeam(idUser!!.toLong(), team_id, team_name, if (join) "join" else "reject")
                }

            }

        })
        notification_recycler.adapter = adapter
        notification_recycler.layoutManager = LinearLayoutManager(activity)
    }
}