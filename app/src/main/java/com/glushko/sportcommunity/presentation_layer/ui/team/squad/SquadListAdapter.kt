package com.glushko.sportcommunity.presentation_layer.ui.team.squad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Squad

class SquadListAdapter(private var list: MutableList<Squad.Params> = mutableListOf(), val callback: Callback) : RecyclerView.Adapter<SquadListAdapter.SquadListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadListViewHolder {
        return SquadListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_squad_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SquadListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class SquadListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tvName: TextView = itemView.findViewById(R.id.tvNameSquad)
        private val tvAmplua: TextView = itemView.findViewById(R.id.tvAmplua)
        private val imgLink: ImageView = itemView.findViewById(R.id.imgLink)
        private val imgInApp: ImageView = itemView.findViewById(R.id.imgInApp)

        fun bind(item: Squad.Params){
            tvName.text = item.user_name
            tvAmplua.text = item.amplua?:""
            imgInApp.visibility = if(item.in_app > 0) View.VISIBLE else View.GONE
            imgLink.visibility = if(item.linked > 0 && item.in_app > 0 ) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                callback.onItemPlayer(item.in_app>0)
            }
        }
    }

    internal fun setList(list: MutableList<Squad.Params>){
        this.list = list
        notifyDataSetChanged()
    }
    interface Callback{
        fun onItemPlayer(inApp: Boolean)
    }
}