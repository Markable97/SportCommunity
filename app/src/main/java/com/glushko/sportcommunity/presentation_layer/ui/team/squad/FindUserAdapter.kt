package com.glushko.sportcommunity.presentation_layer.ui.team.squad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gc.materialdesign.views.ButtonRectangle
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Friend

class FindUserAdapter(private var list: MutableList<Friend.Params> = mutableListOf(),val callback: Callback) : RecyclerView.Adapter<FindUserAdapter.FindUserViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindUserViewHolder {
        return FindUserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_dialog_find_user, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FindUserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class FindUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val userName = itemView.findViewById<TextView>(R.id.tvFindUserName)
        private val btnInvite = itemView.findViewById<ButtonRectangle>(R.id.btnSendInvite)

        fun bind(item: Friend.Params){
            userName.text = item.user_name
            btnInvite.setOnClickListener {
                println("Нажаита кнопка invite для юзера = ${item.user_name}")
                callback.onClickInvite(item.friend_id)
            }
        }
    }

    internal fun setList(list: MutableList<Friend.Params>){
        this.list = list
        notifyDataSetChanged()
    }

    interface Callback{
        fun onClickInvite(user_id: Int)
    }
}