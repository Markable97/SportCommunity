package com.glushko.sportcommunity.presentation_layer.ui.dialog_alert

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException

class DialogAlertMain(private val title: Int,
                      private val listener: DialogAlertMainListener,
                      private val positive_title: String = "Да",
                      private val negative_title: String = "Нет") : DialogFragment() {

    //lateinit var listener: DialogAlertMainListener

    /*override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try{
            listener = activity as DialogAlertMainListener
        }catch (e: ClassCastException){
            println("// The activity doesn't implement the interface, throw exception")
        }
    }*/

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(title)
            .setPositiveButton(positive_title, object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    listener.onPositiveClick()
                }
            })
            .setNegativeButton(negative_title, object  : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    listener.onNegativeClick()
                }

            })
        return builder.create()
    }

    interface DialogAlertMainListener{
        fun onPositiveClick()
        fun onNegativeClick()
    }
}