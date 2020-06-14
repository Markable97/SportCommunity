package com.glushko.sportcommunity.presentation_layer.ui.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.LastMessage
import com.glushko.sportcommunity.presentation_layer.ui.friends.FriendsAdapter
import java.text.SimpleDateFormat
import java.util.*

class ChatsAdapter(private var list: MutableList<LastMessage.Params> = mutableListOf(), val callback: Callback) : RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_chat, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val contactName = itemView.findViewById<TextView>(R.id.tvName)
        private val contactMessage = itemView.findViewById<TextView>(R.id.tvMessage)
        private val contactDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val countNotification = itemView.findViewById<TextView>(R.id.tvNotification)


        @SuppressLint("SimpleDateFormat")
        fun bind(item: LastMessage.Params){
            contactName.text = item.contact_name
            contactMessage.text = item.message
            val sdf = SimpleDateFormat("HH:mm")
            val date = Date(item.message_date * 1000)
            contactDate.text = sdf.format(date)
            if(item.count_notification?:0 > 0 ){
                countNotification.visibility = View.VISIBLE
                countNotification.text = item.count_notification.toString()
            }else{
                countNotification.visibility = View.GONE
            }
            itemView.setOnClickListener{
                callback.onClickChats(list[adapterPosition])
            }
        }
    }

    internal fun setList(list: MutableList<LastMessage.Params>){
        this.list = list
        notifyDataSetChanged()
    }
    interface Callback{
        fun onClickChats(item: LastMessage.Params)
    }
}






