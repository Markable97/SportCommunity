package com.glushko.sportcommunity.presentation_layer.ui.friends_request

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.data_layer.repository.FriendshipNotification

class FriendsRequestAdapter(val callback: Callback) : RecyclerView.Adapter<FriendsRequestAdapter.FriendRequestViewHolder>() {

    private var list: MutableList<FriendshipNotification> = mutableListOf()

    inner class FriendRequestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val tvFriendName = itemView.findViewById<TextView>(R.id.tvFriendRequestName)
        private val btnConfirmFriend = itemView.findViewById<com.gc.materialdesign.views.ButtonRectangle>(R.id.btnConfirmFriend)
        private val btnRejectRequest = itemView.findViewById<com.gc.materialdesign.views.ButtonRectangle>(R.id.btnRejectRequest)

        fun bind(item: FriendshipNotification){
            tvFriendName.text = item.contact_name
            btnConfirmFriend.setOnClickListener {
                callback.onClickConfirm(list[adapterPosition])
            }
            btnRejectRequest.setOnClickListener {
                callback.onClickReject(list[adapterPosition])
            }
        }


    }

    internal fun setList(list: MutableList<FriendshipNotification>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        return FriendRequestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_friend_request, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface Callback{
        fun onClickConfirm(item: FriendshipNotification)
        fun onClickReject(item: FriendshipNotification)
    }
}