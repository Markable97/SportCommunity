package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Event
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel
import java.util.*

class CreateEventDialog: DialogFragment() {

    lateinit var etDateEvent: EditText
    lateinit var etTimeEvent: EditText
    lateinit var etNameEvent: EditText
    lateinit var etLocationEvent: EditText
    lateinit var etPositive: EditText
    lateinit var etNegative: EditText
    lateinit var etNeutral: EditText

    lateinit var modelSquad: SquadViewModel


    lateinit var btnCreateEvent: Button
    companion object{

        const val TAG = "KEY_CREATE_EVENT"
        const val TAG_INT = 2
        const val KEY1 = "team_id"
        const val KEY2 = "user_id"
        const val KEY3 = "team_name"

        fun newInstance(team_id: Long, user_id: Long, team_name: String): CreateEventDialog{
            return CreateEventDialog().apply {
                arguments = Bundle().apply {
                    putLong(KEY1, team_id)
                    putLong(KEY2, user_id)
                    putString(KEY3, team_name)
                }
            }
        }
    }
    private lateinit var timePicker: TimePickerDialog
    private val timeListener = TimePickerDialog.OnTimeSetListener { timePicker, _hour, _minute ->
        val str = "$_hour:$_minute"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelSquad = ViewModelProviders.of(this).get(SquadViewModel::class.java)
        modelSquad.liveDataBaseResponse.observe(this, Observer {
            if(it.success == 1){
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.putExtra(TAG, "UPDATE")
                println("Test = $targetFragment")
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                dismiss()
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { _activity ->
            arguments?.let {

            }
            val builder = AlertDialog.Builder(_activity)
            val view: View = _activity.layoutInflater.inflate(R.layout.dialog_create_event, null)
            etNameEvent = view.findViewById(R.id.et_event_name)
            etLocationEvent = view.findViewById(R.id.et_event_location)
            etPositive = view.findViewById(R.id.et_positive)
            etNegative = view.findViewById(R.id.et_negative)
            etNeutral = view.findViewById(R.id.et_neutral)
            etDateEvent = view.findViewById<EditText>(R.id.et_event_date)
            etTimeEvent = view.findViewById(R.id.et_event_time)
            btnCreateEvent = view.findViewById(R.id.btn_create_event)
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

            btnCreateEvent.setOnClickListener {
                val eventName: String? = if(etNameEvent.text.toString().isEmpty()) null else etNameEvent.text.toString()
                val eventLocation: String? = if(etLocationEvent.text.toString().isEmpty()) null else etLocationEvent.text.toString()
                var date: String? = null
                println("${etDateEvent.text}")
                val dateDop = if (etDateEvent.text.toString().isEmpty()) null  else etDateEvent.text.toString().split(".")
                dateDop?.let {
                    date = "${it[2]}-${it[1]}-${it[0]}"
                }
                val time = if(etTimeEvent.text.toString().isEmpty()) etTimeEvent.text.toString() else null
                val eventDate: String? = if(date != null && time != null) "$date $time:00" else null
                val positiveName: String? = if(etPositive.text.toString().isEmpty()) null else etPositive.text.toString()
                val negativeName: String? = if(etNegative.text.toString().isEmpty()) null else etNegative.text.toString()
                val neutralName: String? = if(etNeutral.text.toString().isEmpty()) null else etNeutral.text.toString()
                var teamId: Long? = null
                var userId: Long? = null
                var teamName: String? = null
                arguments?.let {
                    teamId = it.getLong(KEY1)
                    userId = it.getLong(KEY2)
                    teamName = it.getString(KEY3)
                }
                if(teamId != null && userId != null && eventName != null && teamName != null){
                    val event = Event.Params(
                        team_id = teamId!!,
                        user_id = userId!!,
                        team_name = teamName!!,
                        event_name = eventName,
                        event_location = eventLocation,
                        event_date = eventDate,
                        positive_name = positiveName,
                        negative_name = negativeName,
                        neutral_name = neutralName
                    )
                    println("Данные для сервера по событию: \n $event")
                    modelSquad.createEvent(event)
                }else{
                    Toast.makeText(requireContext(), "Невозможно создать событие :( Заполните поля!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setView(view)
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }

}