package com.glushko.sportcommunity.presentation_layer.ui.dialog

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.vm.DialogViewModel
import com.glushko.sportcommunity.presentation_layer.vm.ModelFactoryForDialog

import kotlinx.android.synthetic.main.fragment_dialog.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.io.File


class DialogFragment(private val friendId: Long, private val count_notification: Int, private val type_dialog: Int) : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_dialog
    lateinit var adapter: DialogAdapter
    lateinit var modelDialog: DialogViewModel
    lateinit var modelDialogFactory: ModelFactoryForDialog
    lateinit var dataDialog: MutableLiveData<ResponseMessage>
    lateinit var LiveDataRepository:  LiveData<List<Message.Params>>

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutId, container, false)
        val context = context ?: return view
        modelDialogFactory = ModelFactoryForDialog(context.applicationContext as Application, friendId)
        modelDialog = ViewModelProviders.of(this, modelDialogFactory).get(DialogViewModel::class.java)
        return view
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user_id: Long? = activity?.getSharedPreferences(activity?.packageName, Context.MODE_PRIVATE)?.let {
            SharedPrefsManager(it).getAccount().idUser.toLong()
        }
        modelDialogFactory = ModelFactoryForDialog(context?.applicationContext as Application, friendId, type_dialog)
        modelDialog = ViewModelProviders.of(this, modelDialogFactory).get(DialogViewModel::class.java)
        adapter = DialogAdapter(friend_id = friendId.toLong(), user_id = user_id?:0)
        dialog_recycle.adapter = adapter
        val manager = LinearLayoutManager(activity)
        manager.reverseLayout = true
        dialog_recycle.layoutManager = manager

        modelDialog.liveDataRepository.observe(viewLifecycleOwner, Observer {
            println("Live data 1\n $it")
            adapter.setList((it as MutableList<Message.Params>))

            dialog_recycle.smoothScrollToPosition(count_notification)
        })

        dataDialog = modelDialog.getData(friendId, type_dialog)//Передать id друга
        dataDialog.observe(viewLifecycleOwner, Observer {
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
                modelDialog.sendMessage(friendId, message, photoFile, photoUri, type_dialog = type_dialog)
            }else{
                Toast.makeText(activity, "Введите сообщение", Toast.LENGTH_SHORT).show()
            }
        }
        btnPhoto.setOnClickListener {
             super.photoFile = createImageFile()
             super.photoUri = createUri(photoFile)
             super.takePhotoIntent(photoUri)
        }
        setEventListener(
            requireActivity(),
            viewLifecycleOwner,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    dialog_recycle.smoothScrollToPosition(0)
                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==TAKE_PICTURE_REQUEST && resultCode == RESULT_OK){
            modelDialog.sendMessage(friendId, "Фотография", photoFile, photoUri, 0, type_dialog)
            //val file = File(currentPhotoPath)
            //val photo: Uri = Uri.fromFile(file)
            //super.saveImage(((ivUserImage.drawable) as BitmapDrawable).bitmap, "${System.currentTimeMillis()}")
        }
    }
}