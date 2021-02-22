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
import com.glushko.sportcommunity.presentation_layer.ui.create_team.TypeSportFragment
import com.glushko.sportcommunity.presentation_layer.vm.FriendsViewModel
import com.glushko.sportcommunity.presentation_layer.vm.NotificationDrawerViewModel
import com.glushko.sportcommunity.presentation_layer.vm.NotificationsViewModel
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : Fragment() {

    val layoutId = R.layout.fragment_notification

    lateinit var adapter: NotificationsAdapter

    lateinit var callback: CallbackNotificationFragment

    private lateinit var modelNotification: NotificationsViewModel
    private lateinit var modelNotificationDrawer: NotificationDrawerViewModel
    private lateinit var modelFriend: FriendsViewModel
    var idUser: Int? = null
    var positionAdapter: Int? = null
    var notificationId: Long? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            callback = context as CallbackNotificationFragment
        }catch(ex: Exception){
            println("Bad callback fragment")
        }
    }

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
        modelNotificationDrawer = ViewModelProviders.of(this).get(NotificationDrawerViewModel::class.java)
        modelNotification = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        modelNotification.liveData.observe(this, Observer {
            println("NotificationFragment Live Data Notification  $it")
            if(it.success == 1){
                adapter.setList(it.notifications)
            }
        })

        modelFriend = ViewModelProviders.of(this).get(FriendsViewModel::class.java)
        modelFriend.liveDataInvitationInTeam.observe(this, Observer {
            println("NotificationFragment Live Data invitation $it")
            if(it.success == 1){
                if(positionAdapter!= null && notificationId != null) {
                    adapter.deletePosition(positionAdapter!!)
                    modelNotificationDrawer.deleteChooseNotification(notificationId!!)
                }

            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modelNotification.getNotification(idUser!!.toLong())

        adapter = NotificationsAdapter(callback = object : NotificationsAdapter.Callback{
            override fun onClickBtn(join: Boolean, position: Int, team_id: Int, team_name: String, notification_id: Long) {
                notificationId = notification_id
                positionAdapter = position
                if(idUser != null){
                    modelFriend.inviteInTeam(idUser!!.toLong(), team_id, team_name, if (join) "join" else "reject")
                }

            }

            override fun onClickNotification(type_notification: String, team_id: Int, team_name: String, notification_id: Long) {
                modelNotificationDrawer.deleteChooseNotification(notification_id)
                callback.onClickEvent(type_notification, team_id.toLong(), team_name, notification_id)
            }

        })
        notification_recycler.adapter = adapter
        notification_recycler.layoutManager = LinearLayoutManager(activity)
    }

    interface CallbackNotificationFragment{
        fun onClickEvent(type_notification: String,
                         team_id: Long,
                         team_name: String,
                         notification_id: Long)
    }
}