package com.glushko.sportcommunity.presentation_layer.ui.team.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Event
import org.w3c.dom.Text

class EventsListAdapter(private var list: MutableList<Event.Params> = mutableListOf()) : RecyclerView.Adapter<EventsListAdapter.EventsListViewHolder>() {


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
            tvEventName.text = item.event_name
            analysisEvent(item)
            analysisUserChoice(item)
        }

        private fun analysisUserChoice(event: Event.Params){
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

    internal fun setList(list: MutableList<Event.Params>){
        this.list = list
        notifyDataSetChanged()
    }
}

