package com.glushko.sportcommunity.presentation_layer.ui.create_team

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class InfoFootballTeamDialog(val title: String, val items: Array<String>) : DialogFragment() {

    companion object{
        const val TAG_ID = "id"
    }


    private var position = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите $title")
                .setSingleChoiceItems(items, -1
                ) { dialog, item ->
                    println("Выбран жлемент $item ${items[item]}")
                    position = item
                }
                .setPositiveButton("OK"
                ) { dialog, id ->
                    val intent = Intent()
                    intent.putExtra(TAG_ID, position)
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                    /*val listener: Callback = targetFragment?.activity as Callback
                    listener?.let { list ->
                        list.onClickOK(position)
                    }*/
                }
                .setNegativeButton("Отмена") {
                        dialog, id -> dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface Callback{
        fun onClickOK(position: Int)
    }
}