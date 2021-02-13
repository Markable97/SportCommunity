package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class ResetChoiceDialog: DialogFragment() {

    companion object{

        const val TAG = "KEY_EVENT"
        const val KEY1 = "event_id"
        const val KEY2 = "choice"
        const val TAG_INT = 1

        fun newInstance(event_id: Long, choice: String): ResetChoiceDialog {
            return ResetChoiceDialog().apply {
                arguments = Bundle().apply {
                    putLong(KEY1, event_id)
                    putString(KEY2, choice)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val intent = Intent()
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Хотите отменить выбор?")
                //.setMessage("Выбери пищу")
                .setPositiveButton("Да") { dialog, id ->
                    intent.putExtra(TAG, "OK")
                    arguments?.let{arg ->
                        intent.putExtra(KEY1, arg.getLong(KEY1))
                        intent.putExtra(KEY2, arg.getString(KEY2))
                    }

                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                }
                .setNegativeButton("Нет",
                    DialogInterface.OnClickListener { dialog, id ->
                        intent.putExtra(TAG, "CANCEL")
                        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        return dialog
    }
}