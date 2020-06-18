package com.glushko.sportcommunity.presentation_layer.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Friend

class FriendsAdapter(private var list: MutableList<Friend.Params> = mutableListOf(),  val callback: Callback) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_friend, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class FriendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val friendName =  itemView.findViewById<TextView>(R.id.tvName)
        private val friendStatus = itemView.findViewById<TextView>(R.id.tvStatus)

        fun bind(item: Friend.Params){
            friendName.text = item.user_name
            friendStatus.text = item.user_status
            itemView.setOnClickListener{
                callback.onClickFriend(list[adapterPosition])
            }
        }
    }

    internal fun setList(list: MutableList<Friend.Params>){
        this.list = list
        notifyDataSetChanged()
    }
    interface Callback{
        fun onClickFriend(item: Friend.Params)
    }
}