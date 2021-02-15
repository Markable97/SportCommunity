package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.glushko.sportcommunity.R
import java.util.*

class CreateEventDialog: DialogFragment() {

    lateinit var etDateEvent: EditText
    lateinit var etTimeEvent: EditText
    companion object{

        const val TAG = "KEY_CREATE_EVENT"

        fun newInstance(): CreateEventDialog{
            return CreateEventDialog()
        }
    }
    private lateinit var timePicker: TimePickerDialog
    private val timeListener = TimePickerDialog.OnTimeSetListener { timePicker, _hour, _minute ->
        val str = "$_hour : $_minute"
        etTimeEvent.setText(str)
    }
    private lateinit var datePicker: DatePickerDialog
    private val dateListener = DatePickerDialog.OnDateSetListener { datePicker, _year, _month, _day ->
        var monthNew = ""
        var dayNew = ""
        if (_month + 1 < 10) {
            monthNew = "0" + (_month + 1)
        }
        if (_day < 10) {
            dayNew = "0$_day"
        }else{
            dayNew = _day.toString()
        }
        val str = "$dayNew.$monthNew.$_year"
        etDateEvent.setText(str)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { _activity ->
            val builder = AlertDialog.Builder(_activity)
            val view: View = _activity.layoutInflater.inflate(R.layout.dialog_create_event, null)
            etDateEvent = view.findViewById<EditText>(R.id.et_event_date)
            etTimeEvent = view.findViewById(R.id.et_event_time)
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            etDateEvent.setOnTouchListener { view, motionEvent ->
                when(motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        datePicker = DatePickerDialog(requireContext(), dateListener, year, month, day)
                        datePicker.show()
                    }
                }

                true
            }
            etTimeEvent.setOnTouchListener { view, motionEvent ->
                when(motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        timePicker = TimePickerDialog(requireContext(), timeListener, hour, minute, true)
                        timePicker.show()
                    }
                }
                true
            }
            builder.setView(view)
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

}