package com.glushko.sportcommunity.presentation_layer.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMessage
import com.glushko.sportcommunity.presentation_layer.vm.DialogViewModel
import kotlinx.android.synthetic.main.fragment_dialog.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class DialogFragment(private val friendId: Int) : Fragment() {

    val layoutId: Int = R.layout.fragment_dialog
    lateinit var adapter: DialogAdapter
    lateinit var modelDialog: DialogViewModel
    lateinit var dataDialog: MutableLiveData<ResponseMessage>
    lateinit var LiveDataRepository:  LiveData<List<Message.Params>>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelDialog = ViewModelProviders.of(this).get(DialogViewModel::class.java)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DialogAdapter(friend_id = friendId.toLong())
        dialog_recycle.adapter = adapter
        val manager = LinearLayoutManager(activity)
        manager.reverseLayout = true
        dialog_recycle.layoutManager = manager

        modelDialog.LiveDataRepository.observe(this, Observer {
            //println("Live data 1")
            adapter.setList((it as MutableList<Message.Params>))
            dialog_recycle.smoothScrollToPosition(0)
        })

        dataDialog = modelDialog.getData(friendId)//Передать id друга
        dataDialog.observe(this, Observer {
            println("Live data 2")
            println("DialogFragment: \n${it.success} ${it.message}, ${it.messages}")
            if(it.success==1){
                etText.text.clear()
                dialog_recycle.smoothScrollToPosition(0)
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        btnSend.setOnClickListener {
            val message = etText.text.toString()
            if(message.isNotEmpty()){
                modelDialog.sendMessage(friendId, message)
            }else{
                Toast.makeText(activity, "Введите сообщение", Toast.LENGTH_SHORT).show()
            }
        }
        setEventListener(
            activity!!,
            viewLifecycleOwner,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    dialog_recycle.smoothScrollToPosition(0)
                }
            })

    }
}