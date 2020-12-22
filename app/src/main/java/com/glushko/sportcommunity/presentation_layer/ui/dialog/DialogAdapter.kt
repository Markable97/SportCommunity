package com.glushko.sportcommunity.presentation_layer.ui.dialog

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Message
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

class DialogAdapter(private var list: MutableList<Message.Params> = mutableListOf(), val friend_id: Long, val user_id: Long):
    RecyclerView.Adapter<DialogAdapter.BaseViewHolder>() {

    fun setList(list: MutableList<Message.Params>){
        this.list = list
        println("Обновляем адаптер")
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].sender_id == friend_id) {
            1
        } else{
            if(list[position].sender_id == user_id){
                0
            }else{
                1
            }
        }

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

    abstract class BaseViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract val tvDate: TextView
        abstract val tvMessage: TextView
        abstract val ivImage: ImageView
        val formating = SimpleDateFormat("HH:mm")
        abstract fun bind(item: Message.Params)
    }

    inner class MessageMeViewHolder(private val itemViewMe: View): BaseViewHolder(itemViewMe){
        override val tvDate: TextView = itemViewMe.findViewById(R.id.tvDate_me)
        override val tvMessage: TextView = itemViewMe.findViewById(R.id.tvMessage_me)
        override val ivImage: ImageView = itemViewMe.findViewById(R.id.imgPhoto_me)
        @SuppressLint("SimpleDateFormat")
        override fun bind(item: Message.Params) {
            val sdf = SimpleDateFormat("HH:mm")
            val date = Date(item.message_date * 2000)
            tvDate.text = sdf.format(date)
            if(item.message_type == 1){
                tvMessage.visibility = View.VISIBLE
                ivImage.visibility = View.GONE
                tvMessage.text = item.message
            }else{
                tvMessage.visibility = View.GONE
                ivImage.visibility = View.VISIBLE
                android.os.Handler().postDelayed({
                    Glide.with(itemViewMe)
                        .load("${NetworkService.BASE_URL_IMAGE_CHAT}/${item.sender_id}_${item.receiver_id}/${item.message_date}.jpg")
                        //.placeholder(R.drawable.ic_healing_black_36dp)
                        .into(ivImage)
                }, 1500)


            }
        }

    }
    inner class MessageOtherViewHolder(private val itemViewOther: View): BaseViewHolder(itemViewOther){
        override val tvDate: TextView = itemViewOther.findViewById(R.id.tvDate_other)
        override val tvMessage: TextView = itemViewOther.findViewById(R.id.tvMessage_other)
        override val ivImage: ImageView = itemViewOther.findViewById(R.id.imgPhoto_other)

        @SuppressLint("SimpleDateFormat")
        override fun bind(item: Message.Params) {
            val sdf = SimpleDateFormat("HH:mm")
            val date = Date(item.message_date * 1000)
            tvDate.text = sdf.format(date)
            if(item.message_type == 1){
                tvMessage.visibility = View.VISIBLE
                ivImage.visibility = View.GONE
                tvMessage.text = item.message
            }else{
                tvMessage.visibility = View.GONE
                ivImage.visibility = View.VISIBLE
                android.os.Handler().postDelayed({
                    Glide.with(itemViewOther)
                        .load("${NetworkService.BASE_URL_IMAGE_CHAT}${item.sender_id}_${item.receiver_id}/${item.message_date}.jpg")
                        .error(R.drawable.ic_healing_black_36dp)
                        //.placeholder(R.drawable.ic_healing_black_36dp)
                        .into(ivImage)
                }, 1500)


            }
        }

    }
}