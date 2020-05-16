package com.glushko.sportcommunity.presentation_layer.ui.profile

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.TeamsUserInfo
import com.glushko.sportcommunity.data_layer.datasource.NetworkService


class ProfileTeamsAdapter(private var list: MutableList<TeamsUserInfo.Params>, val callback: Callback) : RecyclerView.Adapter<ProfileTeamsAdapter.TeamViewHolder>() {
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

        private val imageTeam = itemView.findViewById<ImageView>(R.id.imageTeam)
        private val teamName = itemView.findViewById<TextView>(R.id.item_team_name)
        private val amplua = itemView.findViewById<TextView>(R.id.iteam_amplua)
        private val statistics = itemView.findViewById<TextView>(R.id.item_statistics)


        fun bind(item: TeamsUserInfo.Params){
            teamName.text = item.team_name
            amplua.text = item.amplua
            statistics.text = "Игры ${item.games}, Голы ${item.goals}, Передачи ${item.assists}, ЖК ${item.yellow}, КК ${item.red}"
            Glide.with(itemView.context)
                .load(NetworkService.BASE_URL_IMAGE+item.team_name+".png")
                .placeholder(R.drawable.chatplaceholder)
                .into(imageTeam)
            itemView.setOnClickListener{
                val bitmap: Bitmap = imageTeam.drawable.toBitmap()
                callback.onItemClicked(list[adapterPosition], bitmap)
            }
        }
    }

    internal fun setList(list: MutableList<TeamsUserInfo.Params>){
        this.list = list
        notifyDataSetChanged()
    }
    interface Callback{
        fun onItemClicked(item:  TeamsUserInfo.Params, bitmap: Bitmap)
    }
}