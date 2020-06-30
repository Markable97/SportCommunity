package com.glushko.sportcommunity.presentation_layer.ui.friends_request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.data_layer.repository.FriendshipNotification
import com.glushko.sportcommunity.presentation_layer.vm.FriendsViewModel
import kotlinx.android.synthetic.main.fragment_friends_request.*

class FriendsRequestFragment: Fragment() {
    val layoutId: Int = R.layout.fragment_friends_request

    lateinit var adapter: FriendsRequestAdapter
    lateinit var modelFriend: FriendsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelFriend = ViewModelProviders.of(this).get(FriendsViewModel::class.java)

        modelFriend.liveDataNotification.observe(this, Observer {
            println("Live data 3")
            println("Request friend: $it")
            adapter.setList(it.toMutableList())
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FriendsRequestAdapter(object : FriendsRequestAdapter.Callback{
            override fun onClickConfirm(item: FriendshipNotification) {
                println("подтверждаю друга")
            }

            override fun onClickReject(item: FriendshipNotification) {
                println("отклоняю друга")
            }

        })

        friends_request_recycler.adapter = adapter
        friends_request_recycler.layoutManager = LinearLayoutManager(activity)
    }
}