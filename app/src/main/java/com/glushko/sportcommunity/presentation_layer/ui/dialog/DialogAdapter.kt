package com.glushko.sportcommunity.presentation_layer.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import java.text.SimpleDateFormat
import java.util.*

class DialogAdapter(private var list: MutableList<Message.Params> = mutableListOf(), val friend_id: Long):
    RecyclerView.Adapter<DialogAdapter.BaseViewHolder>() {

    fun setList(list: MutableList<Message.Params>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].sender_id == friend_id) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val holder = if(viewType == 0){
            MessageMeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_message_me, parent, false))
        }else{
            MessageOtherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_message_other, parent, false))
        }
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(list[position])
    }

    abstract class BaseViewHolder(protected val itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract val tvDate: TextView
        abstract val tvMessage: TextView
        val formating = SimpleDateFormat("HH:mm")
        abstract fun bind(item: Message.Params)
    }

    inner class MessageMeViewHolder(itemView: View
    ): BaseViewHolder(itemView){
        override val tvDate: TextView = itemView.findViewById(R.id.tvDate_me)
        override val tvMessage: TextView = itemView.findViewById(R.id.tvMessage_me)

        override fun bind(item: Message.Params) {
            val date = Date(item.message_date)
            tvDate.text = formating.format(date.time)
            tvMessage.text = item.message
        }

    }
    inner class MessageOtherViewHolder(itemView: View): BaseViewHolder(itemView){
        override val tvDate: TextView = itemView.findViewById(R.id.tvDate_other)
        override val tvMessage: TextView = itemView.findViewById(R.id.tvMessage_other)

        override fun bind(item: Message.Params) {
            val date = Date(item.message_date)
            tvDate.text = formating.format(date.time)
            tvMessage.text = item.message
        }

    }
}