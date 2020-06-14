package com.glushko.sportcommunity.presentation_layer.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseLastMessage
import com.glushko.sportcommunity.presentation_layer.vm.ChatsViewModel
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatsFragment(val callback: Callback) : Fragment() {
    val layoutId = R.layout.fragment_chat

    lateinit var adapter: ChatsAdapter
    lateinit var modelChats: ChatsViewModel
    lateinit var dataChats: MutableLiveData<ResponseLastMessage>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelChats = ViewModelProviders.of(this).get(ChatsViewModel::class.java)

        modelChats.liveDataRepository.observe(this, Observer {
            println("Live data 1 update adapter \n $it")
            adapter.setList(it as MutableList<LastMessage.Params>)
        })

        dataChats = modelChats.getData()
        dataChats.observe(this, Observer {
            println("Live data 2")
            println("ChatsFragment: \n${it.success} ${it.message}, ${it.lastMessages}")
            if(it.success == 1){
                //adapter.setList(it.friends)
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatsAdapter(callback = object : ChatsAdapter.Callback{
            override fun onClickChats(item: LastMessage.Params) {
                println("ответ от recycler = ${item.contact_id}")

                callback.changeFragment(item.contact_id, item.contact_name)
            }

        })
        chats_recycler.adapter = adapter
        chats_recycler.layoutManager = LinearLayoutManager(activity)

    }


    interface Callback{
        fun changeFragment(contact_id: Long, contact_name: String)
    }
}