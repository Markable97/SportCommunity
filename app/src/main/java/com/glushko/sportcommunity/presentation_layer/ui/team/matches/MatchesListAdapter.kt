package com.glushko.sportcommunity.presentation_layer.ui.team.matches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.MatchFootball
import com.glushko.sportcommunity.data_layer.datasource.NetworkService


class MatchesListAdapter (private var list: List<MatchFootball> = mutableListOf(), val callback: Callback) : RecyclerView.Adapter<MatchesListAdapter.BaseViewHolder>(){

    fun update(matches: List<MatchFootball>){
        this.list = matches
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].played // 1 - матч сыгран (результат) 0 - матч не сыгран (календарь)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if(viewType == 1) ResultsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_results, parent, false))
        else CalendarViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_calendar, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(list[position])
    }

    abstract class BaseViewHolder(open val itemView: View) : RecyclerView.ViewHolder(itemView){

        abstract val tvMatchDescription: TextView
        abstract val tvTour: TextView
        abstract val imgTeamHome: ImageView
        abstract val imgTeamGuest: ImageView
        abstract val tvTeamHomeName: TextView
        abstract val tvTeamGuestName: TextView
        abstract val tvUpperDesc: TextView
        abstract val tvDawnDesc: TextView


        abstract fun bind(match: MatchFootball)
    }

    inner class ResultsViewHolder(private val itemViewRes: View): BaseViewHolder(itemViewRes){
        override val tvMatchDescription: TextView = itemViewRes.findViewById(R.id.res_name_lig_div_date)
        override val tvTour: TextView = itemViewRes.findViewById(R.id.res_tour)
        override val imgTeamHome: ImageView = itemViewRes.findViewById(R.id.res_image_home)
        override val tvTeamHomeName: TextView = itemViewRes.findViewById(R.id.res_name_home)
        override val tvUpperDesc: TextView = itemViewRes.findViewById(R.id.res_goal_home)
        override val imgTeamGuest: ImageView = itemViewRes.findViewById(R.id.res_image_visit)
        override val tvTeamGuestName: TextView = itemViewRes.findViewById(R.id.res_name_visit)
        override val tvDawnDesc: TextView = itemViewRes.findViewById(R.id.res_goal_visit)


        override fun bind(match: MatchFootball) {
            tvMatchDescription.text = "${match.league_name} ${match.division_name} ${match.match_date?:""}"
            tvTour.text = match.tour_id.toString()
            Glide.with(itemViewRes.context)
                .load(NetworkService.BASE_URL_IMAGE+match.team_home_name+".png")
                .placeholder(R.drawable.chatplaceholder)
                .into(imgTeamHome)
            tvTeamHomeName.text = match.team_home_name
            tvUpperDesc.text = match.goal_home.toString()
            Glide.with(itemViewRes.context)
                .load(NetworkService.BASE_URL_IMAGE+match.team_guest_name+".png")
                .placeholder(R.drawable.chatplaceholder)
                .into(imgTeamGuest)
            tvTeamGuestName.text = match.team_guest_name
            tvDawnDesc.text = match.goal_guest.toString()
        }

    }

    inner class CalendarViewHolder(private val itemViewCal: View): BaseViewHolder(itemViewCal){
        override val tvMatchDescription: TextView = itemViewCal.findViewById(R.id.cal_name_lig_div)
        override val tvTour: TextView = itemViewCal.findViewById(R.id.cal_tour)
        override val imgTeamHome: ImageView = itemViewCal.findViewById(R.id.cal_image_home)
        override val tvTeamHomeName: TextView = itemViewCal.findViewById(R.id.cal_name_home)
        override val imgTeamGuest: ImageView = itemViewCal.findViewById(R.id.cal_image_visit)
        override val tvTeamGuestName: TextView = itemViewCal.findViewById(R.id.cal_name_visit)
        override val tvUpperDesc: TextView = itemViewCal.findViewById(R.id.cal_date)
        override val tvDawnDesc: TextView = itemViewCal.findViewById(R.id.cal_stadium)

        override fun bind(match: MatchFootball) {
            tvMatchDescription.text = "${match.league_name} ${match.division_name} "
            tvTour.text = match.tour_id.toString()
            Glide.with(itemViewCal.context)
                .load(NetworkService.BASE_URL_IMAGE+match.team_home_name+".png")
                .placeholder(R.drawable.chatplaceholder)
                .into(imgTeamHome)
            tvTeamHomeName.text = match.team_home_name
            tvUpperDesc.text = match.match_date
            Glide.with(itemViewCal.context)
                .load(NetworkService.BASE_URL_IMAGE+match.team_guest_name+".png")
                .placeholder(R.drawable.chatplaceholder)
                .into(imgTeamGuest)
            tvTeamGuestName.text = match.team_guest_name
            tvDawnDesc.text = match.name_stadium
        }
    }
    interface Callback{
        fun onItemResult(match_id: Long)
    }
}