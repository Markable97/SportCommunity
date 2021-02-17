package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Event
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel
import kotlinx.android.synthetic.main.fragment_team_events.*
import kotlinx.android.synthetic.main.fragment_team_events_content_main.*

class EventsFragment(private val team_id: Long, private val team_name: String,  private val isLeader: Boolean): Fragment() {

    val layoutId: Int = R.layout.fragment_team_events

    lateinit var modelSquad: SquadViewModel

    //private var dialogCreateEvent: CreateEventDialog? = null

    var eventsList: MutableList<Event.Params> = mutableListOf()

    var adapter: EventsListAdapter? = null

    var positionDeleteEvent: Int? = null

    var adapterPosition: Int? = null
    var changeChoice: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelSquad = ViewModelProviders.of(this).get(SquadViewModel::class.java)

        modelSquad.liveDataEventsList.observe(this, Observer {
            println("Live data squad list ${it.success} ${it.message}")
            if(it.success == 1){
                eventsList = it.events
                adapter?.setList(eventsList)
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        modelSquad.liveDataBaseResponse.observe(this, Observer {
            println("Live date delete ${it.message}")
            if(it.success == 1){
                when(it.message){
                    "Event deleted" -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                        eventsList.removeAt(positionDeleteEvent!!)
                        adapter?.deleteEvent(positionDeleteEvent!!, eventsList)
                    }
                    "Choice deleted" ->{
                        adapterPosition?.let {position ->
                            eventsList[position].also {event ->
                                event.user_choice = "none"
                                when(changeChoice!!){
                                    "positive" -> event.positive_count = event.positive_count - 1
                                    "negative" -> event.negative_count = event.negative_count - 1
                                    "neutral" -> event.neutral_count = event.neutral_count - 1
                                }
                            }
                            adapter?.setList(eventsList)
                        }

                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    "Choice inserted"->{
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                        adapterPosition?.let {position ->
                            eventsList[position].also {event ->
                                  event.user_choice = changeChoice!!
                                  when(changeChoice!!){
                                      "positive" -> event.positive_count = event.positive_count + 1
                                      "negative" -> event.negative_count = event.negative_count + 1
                                      "neutral" -> event.neutral_count = event.neutral_count + 1
                                  }
                            }
                            adapter?.setList(eventsList)
                        }
                    }

                }

            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                adapter?.setList(eventsList)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modelSquad.getEventsList(team_id)

        adapter = EventsListAdapter(callback = object :EventsListAdapter.Callback{
            override fun onClickDeleteEvent(idEvent: Long, position: Int) {
                modelSquad.deleteEvent(idEvent)
                positionDeleteEvent = position
            }

            override fun onClickChoice(
                event: Event.Params,
                position: Int,
                choiceMode: String,
                choice: String
            ) {
                adapterPosition = position
                changeChoice = choice
                if(choiceMode == "insert"){
                    modelSquad.modifyChoice(event.event_id, choiceMode, choice)
                }else{
                    val dialogReset = ResetChoiceDialog.newInstance(event.event_id, choice)
                    dialogReset.setTargetFragment(this@EventsFragment, ResetChoiceDialog.TAG_INT)
                    dialogReset.isCancelable = false
                    val manager = parentFragmentManager
                    dialogReset.show(manager, ResetChoiceDialog.TAG)
                }
            }


        })

        events_team_recycler.adapter = adapter
        events_team_recycler.layoutManager = LinearLayoutManager(activity)

        if(isLeader){
            fab_add_event.setOnClickListener {
                val dialogCreateEvent = CreateEventDialog.newInstance(team_id, modelSquad.idUser, team_name)
                dialogCreateEvent.setTargetFragment(this@EventsFragment, CreateEventDialog.TAG_INT)
                val manager = parentFragmentManager
                dialogCreateEvent.show(manager, CreateEventDialog.TAG)
            }
        }else{
            fab_add_event.visibility = View.GONE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("Events Fragment: onActivityResult")
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                ResetChoiceDialog.TAG_INT ->{
                    val result = data?.getStringExtra(ResetChoiceDialog.TAG)
                    if(result == "OK"){
                        val eventId = data.getLongExtra(ResetChoiceDialog.KEY1, 0)
                        val choice = data.getStringExtra(ResetChoiceDialog.KEY2)
                        modelSquad.modifyChoice(eventId, "delete",choice)
                    }else{
                        adapter?.setList(eventsList)
                    }
                }
                CreateEventDialog.TAG_INT ->{
                    val result = data?.getStringExtra(CreateEventDialog.TAG)
                    if (result == "UPDATE"){
                        modelSquad.getEventsList(team_id)
                    }
                }
            }
        }
    }


}