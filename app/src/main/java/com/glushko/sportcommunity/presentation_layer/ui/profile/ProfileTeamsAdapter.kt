package com.glushko.sportcommunity.presentation_layer.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.TeamPlayer


class ProfileTeamsAdapter(var list: MutableList<TeamPlayer>, val callback: Callback) : RecyclerView.Adapter<ProfileTeamsAdapter.TeamViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileTeamsAdapter.TeamViewHolder {
        return TeamViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_team, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProfileTeamsAdapter.TeamViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class TeamViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val teamName = itemView.findViewById<TextView>(R.id.item_team_name)
        private val amplua = itemView.findViewById<TextView>(R.id.iteam_amplua)
        private val statistics = itemView.findViewById<TextView>(R.id.item_statistics)

        fun bind(item: TeamPlayer){
            teamName.text = item.teamName
            amplua.text = item.amplua
            statistics.text = item.statistics
            itemView.setOnClickListener{
                callback.onItemCkicked(list[adapterPosition])
            }
        }
    }

    interface Callback{
        fun onItemCkicked(item: TeamPlayer)
    }
}