package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Event
import com.realpacific.clickshrinkeffect.applyClickShrink


class EventsListAdapter(private var list: MutableList<Event.Params> = mutableListOf(), val callback: Callback) : RecyclerView.Adapter<EventsListAdapter.EventsListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsListViewHolder {
        return EventsListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_team_events, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EventsListViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class EventsListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val btnDeleteEvent: ImageButton = itemView.findViewById(R.id.btn_delete_event)

        private val tvEventName: TextView = itemView.findViewById(R.id.tv_event_name)
        private val tvEventDate: TextView = itemView.findViewById(R.id.tv_event_date)
        private val tvEventLocation: TextView = itemView.findViewById(R.id.tv_event_location)
        //Positive
        private val rbPositiveChoice: RadioButton = itemView.findViewById(R.id.rb_positive_choice)
        private val tvPositiveName: TextView = itemView.findViewById(R.id.tv_positive_name)
        private val pbPositive: ProgressBar = itemView.findViewById(R.id.pb_positive)
        private val tvCntPositive: TextView = itemView.findViewById(R.id.tv_cnt_positive)
        //Negative
        private val rbNegativeChoice: RadioButton = itemView.findViewById(R.id.rb_negative_choice)
        private val tvNegativeName: TextView = itemView.findViewById(R.id.tv_negative_name)
        private val pbNegative: ProgressBar = itemView.findViewById(R.id.pb_negative)
        private val tvCntNegative: TextView = itemView.findViewById(R.id.tv_cnt_negative)
        //Neutral
        private val rbNeutralChoice: RadioButton = itemView.findViewById(R.id.rb_neutral_choice)
        private val tvNeutralName: TextView = itemView.findViewById(R.id.tv_neutral_name)
        private val pbNeutral: ProgressBar = itemView.findViewById(R.id.pb_neutral)
        private val tvCntNeutral: TextView = itemView.findViewById(R.id.tv_cnt_neutral)


        fun bind(item: Event.Params){
            btnDeleteEvent.applyClickShrink()
            tvEventName.text = item.event_name
            analysisEvent(item)
            analysisUserChoice(item)
            val choice = analysisRadioButton()
            if(item.is_leader == 0){
                btnDeleteEvent.visibility = View.INVISIBLE
            }
            btnDeleteEvent.setOnClickListener {
                callback.onClickDeleteEvent(item.event_id, absoluteAdapterPosition)
            }
            rbPositiveChoice.setOnClickListener {
                eventActions(item, "positive",choice)
            }
            rbNegativeChoice.setOnClickListener {
                eventActions(item, "negative",choice)
            }
            rbNeutralChoice.setOnClickListener {
                eventActions(item, "neutral", choice)
            }
        }

        private fun eventActions(event: Event.Params, choiceButton: String, choice: String){

            if(choice == "none"){
                callback.onClickChoice(event/*.also {
                    it.user_choice = choiceButton
                    when(choiceButton){
                        "positive" -> it.positive_count = it.positive_count + 1
                        "negative" -> it.negative_count = it.negative_count + 1
                        "neutral" -> it.neutral_count = it.neutral_count + 1
                    }
                }*/, absoluteAdapterPosition, "insert", choiceButton)
            }else{
                /*rbPositiveChoice.isChecked = false
                rbNegativeChoice.isChecked = false
                rbNeutralChoice.isChecked = false*/
                /*val eventDop = event
                eventDop.also {
                    it.user_choice = "none"
                    when(choice){
                        "positive" -> it.positive_count = it.positive_count - 1
                        "negative" -> it.negative_count = it.negative_count - 1
                        "neutral" -> it.neutral_count = it.neutral_count- 1
                    }
                }*/
                callback.onClickChoice(event, absoluteAdapterPosition, "delete", choice)

            }
        }

        private fun analysisRadioButton():String{

            if (rbPositiveChoice.isChecked){
                return "positive"
            }
            if(rbNegativeChoice.isChecked){
                return "negative"
            }
            if(rbNeutralChoice.isChecked){
                return "negative"
            }

            return "none"
        }

        private fun analysisUserChoice(event: Event.Params){
            rbPositiveChoice.isChecked = false
            rbNegativeChoice.isChecked = false
            rbNeutralChoice.isChecked = false
            when(event.user_choice){
                "positive" -> rbPositiveChoice.isChecked = true
                "negative" -> rbNegativeChoice.isChecked = true
                "neutral" -> rbNeutralChoice.isChecked = true
            }
        }

        private fun analysisEvent(event: Event.Params){
            if (event.event_date != null){
                tvEventDate.text = event.event_date
            }else{
                tvEventDate.text = "-------------------"
            }
            if (event.event_location != null){
                tvEventLocation.text = event.event_location
            }else{
                tvEventLocation.text = "-------------------"
            }

            //Утвержда. что не будет положительного, не будет и отрицательного
            event.positive_name?.let {
                rbPositiveChoice.visibility = View.VISIBLE
                tvPositiveName.visibility = View.VISIBLE
                tvPositiveName.text = it
                pbPositive.visibility = View.VISIBLE
                tvCntPositive.visibility = View.VISIBLE
                tvCntPositive.text = event.positive_count.toString()
                event.negative_name?.let{negative: String ->
                    rbNegativeChoice.visibility = View.VISIBLE
                    tvNegativeName.visibility = View.VISIBLE
                    tvNegativeName.text = negative
                    pbNegative.visibility = View.VISIBLE
                    tvCntNegative.visibility = View.VISIBLE
                    tvCntNegative.text = event.negative_count.toString()
                    event.neutral_name?.let{neutral:String ->
                        rbNeutralChoice.visibility = View.VISIBLE
                        tvNeutralName.visibility = View.VISIBLE
                        tvNeutralName.text = neutral
                        pbNeutral.visibility = View.VISIBLE
                        tvCntNeutral.visibility = View.VISIBLE
                        tvCntNeutral.text = event.neutral_count.toString()
                    }
                }
            }
        }
    }

    internal fun deleteEvent(position: Int, list: MutableList<Event.Params>){
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    internal fun setList(list: MutableList<Event.Params>){
        this.list = list
        notifyDataSetChanged()
    }

    interface Callback{
        fun onClickDeleteEvent(idEvent: Long, position: Int)
        fun onClickChoice(event: Event.Params, position: Int, choiceMode: String, choice: String)
    }
}

