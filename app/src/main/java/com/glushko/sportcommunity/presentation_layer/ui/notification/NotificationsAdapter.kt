package com.glushko.sportcommunity.presentation_layer.ui.notification

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Notification
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import com.tsuryo.swipeablerv.SwipeLeftRightCallback

class NotificationsAdapter(private var list: MutableList<Notification.Params> = mutableListOf(), val callback: Callback): RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>(){

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        context = parent.context
        return NotificationsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_notification_invite_team, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class NotificationsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val imageTeam: ImageView = itemView.findViewById(R.id.imgTeamNotification)
        private val tvTextNotification: TextView = itemView.findViewById(R.id.tvTextInvitation)
        private val btnJoin: Button = itemView.findViewById(R.id.btnJoin)
        private val btnReject: Button = itemView.findViewById(R.id.btnReject)

        fun bind(item: Notification.Params){
            itemView.context?.apply {
                if(item.notification_type != "event"){
                    val rsStr = getString(R.string.message_invite_team)
                    val fsStr = String.format(rsStr, item.team_name)
                    val styleStr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)  Html.fromHtml(fsStr, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(fsStr)
                    tvTextNotification.text = styleStr
                }else{
                    val rsStr = getString(R.string.message_event_team)
                    val fsStr = String.format(rsStr, item.team_name, item.event_name)
                    val styleStr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)  Html.fromHtml(fsStr, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(fsStr)
                    tvTextNotification.text = styleStr
                    btnJoin.visibility = View.GONE
                    btnReject.visibility = View.GONE
                }
            }
            Glide.with(itemView.context)
                .load(NetworkService.BASE_URL_IMAGE+item.team_name+".png")
                .placeholder(R.drawable.chatplaceholder)
                .into(imageTeam)
            itemView.setOnClickListener {
                callback.onClickNotification(item.notification_type, item.team_id, item.team_name, item.notification_id)
            }
            btnJoin.setOnClickListener {
                callback.onClickBtn(true, bindingAdapterPosition, item.team_id, item.team_name, item.notification_id)
            }
            btnReject.setOnClickListener {
                callback.onClickBtn(false, bindingAdapterPosition, item.team_id, item.team_name, item.notification_id)
            }

        }
    }


    internal fun setList(list: MutableList<Notification.Params>){
        this.list = list
        notifyDataSetChanged()
    }
    internal fun deletePosition(position: Int){
        this.list.removeAt(position)
        notifyDataSetChanged()
    }

    interface Callback{
        fun onClickBtn(join: Boolean, position: Int, team_id: Int, team_name: String, notification_id: Long)
        fun onClickNotification(type_notification: String, team_id: Int, team_name: String, notification_id: Long)
    }

}