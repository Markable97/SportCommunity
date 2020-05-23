package com.glushko.sportcommunity.presentation_layer.ui.friends

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Friend
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFriends
import com.glushko.sportcommunity.presentation_layer.vm.FriendsViewModel
import kotlinx.android.synthetic.main.fragment_friends.*

class FriendsFragment(val callback: Callback) : Fragment() {

    val layoutId: Int = R.layout.fragment_friends

    lateinit var adapter: FriendsAdapter

    lateinit var modelFriend: FriendsViewModel
    lateinit var dataFriends: MutableLiveData<ResponseFriends>
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

        modelFriend.liveDataRepository.observe(this, Observer {
            println("Live data 1")
            adapter.setList((it as MutableList<Friend.Params>))
        })
        dataFriends = modelFriend.getData()
        dataFriends.observe(this, Observer {
            println("Live data 2")
            println("FriendFragment: \n${it.success} ${it.message}, ${it.friends}")
            if(it.success == 1){
                //adapter.setList(it.friends)
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FriendsAdapter(callback = object : FriendsAdapter.Callback{
            override fun onClickFriend(item: Friend.Params) {
                callback.changeFragment(item.friend_id, item.friend_name)
            }

        })
        friends_recycler.adapter = adapter
        friends_recycler.layoutManager = LinearLayoutManager(activity)
    }

    interface Callback{
        fun changeFragment(friend_id: Int, friend_name: String)
    }
}