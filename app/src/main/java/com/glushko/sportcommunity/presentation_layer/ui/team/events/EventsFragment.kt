package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Event
import com.glushko.sportcommunity.presentation_layer.vm.SquadViewModel
import kotlinx.android.synthetic.main.fragment_team_events_content_main.*

class EventsFragment(private val team_id: Long, private val team_name: String): Fragment() {

    val layoutId: Int = R.layout.fragment_team_events

    lateinit var modelSquad: SquadViewModel

    var eventsList: MutableList<Event.Params> = mutableListOf()

    var adapter: EventsListAdapter? = null

    var positionDeleteEvent: Int? = null

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
                Toast.makeText(activity, "delete event", Toast.LENGTH_SHORT).show()
                eventsList.removeAt(positionDeleteEvent!!)
                adapter?.deleteEvent(positionDeleteEvent!!, eventsList)
            }else{
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        modelSquad.getEventsList(team_id)

        adapter = EventsListAdapter(callback = object :EventsListAdapter.Callback{
            override fun deleteEvent(idEvent: Long, position: Int) {
                modelSquad.deleteEvent(idEvent)
                positionDeleteEvent = position
            }

        })

        events_team_recycler.adapter = adapter
        events_team_recycler.layoutManager = LinearLayoutManager(activity)

    }

}