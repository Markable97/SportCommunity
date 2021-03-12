package com.glushko.sportcommunity.presentation_layer.ui.tournament_table
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.TournamentTableFootball
import com.glushko.sportcommunity.data_layer.datasource.NetworkService
import kotlinx.android.synthetic.main.fragment_tournament_table_football.*

class TournamentTableFootballFragment: TournamentTableFragment() {

    override val layoutId: Int = R.layout.fragment_tournament_table_football

    companion object{

        const val TAG = "KEY_TOURNAMENT_TABLE_FOOTBALL"
        const val KEY1 = "team_id"

        fun newInstance(team_id: Long):TournamentTableFootballFragment{
            return TournamentTableFootballFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY1, team_id)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.liveDataTable.observe(this, Observer {
            if(it.success == 1){
                drawTable(it.tournament_table)
            }else{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })

        arguments?.let {
            model.getTournamentTableFootball(team_id = it.getLong(KEY1))
        }
    }

    private fun drawTable(list: MutableList<TournamentTableFootball.Params>){
        tableForDiv.removeAllViews()

        val countColumn = 9
        val _1dp = 0
        val whiteColor = Color.WHITE

        for (i in -1 until list.size) {
            //Log.i(TAG, "createTable: Строка = " + i);
            val tableRow = TableRow(activity)
            tableRow.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            //tableRow.setBackgroundResource();
            for (j in 0 until countColumn) {
                if (i == -1) {
                    //Log.i(TAG, "createTable: Столбец = " + j);
                    val params =
                        TableRow.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    //params.gravity = Gravity.CENTER;
                    params.setMargins(_1dp, _1dp, _1dp, _1dp)
                    val tv = TextView(activity)
                    tv.layoutParams = params
                    tv.gravity = Gravity.CENTER
                    tv.setBackgroundColor(whiteColor)
                    when (j) {
                        0 -> tv.text = "#"
                        1 -> tv.text = "Лого"
                        2 -> tv.text = "Команда"
                        3 -> tv.text = "И"
                        4 -> tv.text = "В"
                        5 -> tv.text = "Н"
                        6 -> tv.text = "П"
                        7 -> tv.text = "Р/М"
                        8 -> tv.text = "О"
                    }
                    tableRow.addView(tv, j)
                } else {
                    //Log.i(TAG, "createTable: Столбец = " + j);
                    val params =
                        TableRow.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    //params.gravity = Gravity.CENTER;
                    val tv = TextView(activity)
                    params.setMargins(_1dp, _1dp, _1dp, _1dp)
                    tv.layoutParams = params
                    tv.gravity = Gravity.CENTER
                    /*tv.setBackgroundColor(
                        changeColor(
                            newTournamentTable.get(i).getDivisionName(),
                            i
                        )
                    )*/
                    when (j) {
                        0 -> tv.text = (i + 1).toString()
                        1 -> {
                            val paramsImage =
                                TableRow.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            //paramsImage.gravity = Gravity.CENTER;
                            paramsImage.setMargins(_1dp, _1dp, _1dp, _1dp)
                            val imageView =
                                ImageView(activity)
                            /*imageView.setBackgroundColor(
                                changeColor(
                                    newTournamentTable.get(i).getDivisionName(), i
                                )
                            )*/
                            Glide.with(requireContext())
                                .load(NetworkService.BASE_URL_IMAGE+list[i].team_name+".png")
                                .placeholder(R.drawable.chatplaceholder)
                                .into(imageView)
                            imageView.layoutParams = paramsImage
                            tableRow.addView(imageView, j)
                        }
                        2 -> {
                            tv.text = (list[i].team_name)
                            tv.setOnClickListener {
                                Toast.makeText(requireContext(), "Переход на команду", Toast.LENGTH_SHORT).show()
                                /*val intent =
                                    Intent(activity, TeamActivity::class.java)
                                intent.putExtra(
                                    "dateForActivity",
                                    newTournamentTable.get(i).getImageBase64()
                                )
                                intent.putExtra(
                                "dataForActivityName",
                                tv.text
                            )
                                intent.putExtra(
                                    "dataForActivityID",
                                    newTournamentTable.get(i).getIdTeam()
                                )
                                startActivity(intent)*/
                            }
                        }
                        3 -> tv.text = list[i].games.toString()
                        4 -> tv.text = list[i].wins.toString()
                        5 -> tv.text = list[i].draws.toString()
                        6 -> tv.text = list[i].losses.toString()
                        7 -> tv.text = list[i].sc_con.toString()
                        8 -> tv.text = list[i].points.toString()
                    }
                    //Log.i(TAG, "onCreateView: TV = " + tv.getText());
                    if (j != 1) {
                        tableRow.addView(tv, j)
                    }
                }
            }
            tableForDiv.addView(tableRow, i + 1)
        }
    }

}