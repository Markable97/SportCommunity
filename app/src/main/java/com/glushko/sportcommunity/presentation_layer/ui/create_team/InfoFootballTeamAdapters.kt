package com.glushko.sportcommunity.presentation_layer.ui.create_team

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballDivisions
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballLeagues
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballTeams


open class CustomLeaguesAdapter(val context: Context, var leagues: MutableList<ResponseFootballLeagues.Params>, var resource: Int) : /*ArrayAdapter<ResponseFootballLeagues.Params>(context, resource, leagues)*/BaseAdapter(), SpinnerAdapter {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val convertView = View.inflate(context, R.layout.spinner_item_info_teams, null)
        val tv_name_league: TextView = convertView.findViewById(R.id.tv_item_spinner)
        tv_name_league.text = leagues[position].league_name

        return convertView!!
    }

    override fun getItem(position: Int): ResponseFootballLeagues.Params {
        return leagues[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return leagues.size
    }

    companion object{
        var flag: Boolean = false
    }

}
class CustomDivisionsAdapter(val context: Context, var divisions: MutableList<ResponseFootballDivisions.Params>) : BaseAdapter(), SpinnerAdapter{
    override fun getView(position: Int, view: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.spinner_item_info_teams, null)
        val tv_name_league: TextView = view.findViewById(R.id.tv_item_spinner)
        tv_name_league.text = divisions[position].division_name
        return view
    }

    override fun getItem(position: Int): Any {
        return divisions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return divisions.size
    }
}

class CustomTeamsAdapter(val context: Context, var teams: MutableList<ResponseFootballTeams.Params>) : BaseAdapter(), SpinnerAdapter{
    override fun getView(position: Int, view: View?, p2: ViewGroup?): View {
        val view = View.inflate(context, R.layout.spinner_item_info_teams, null)
        val tv_name_league: TextView = view.findViewById(R.id.tv_item_spinner)
        tv_name_league.text = teams[position].team_name
        return view
    }

    override fun getItem(position: Int): Any {
        return teams[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return teams.size
    }


}