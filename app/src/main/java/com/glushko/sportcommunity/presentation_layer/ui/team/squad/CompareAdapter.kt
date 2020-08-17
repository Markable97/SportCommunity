package com.glushko.sportcommunity.presentation_layer.ui.team.squad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Squad
import de.hdodenhof.circleimageview.CircleImageView

class CompareAdapter(private val type_item: Int, private var list: MutableList<Squad.Params> = mutableListOf() ,val callback: Callback): RecyclerView.Adapter<CompareAdapter.CompareAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompareAdapterViewHolder {
         return CompareAdapterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_compare_user, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CompareAdapterViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class CompareAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val tvName =  itemView.findViewById<TextView>(R.id.tvNameCompare)
        private val tvAmplua = itemView.findViewById<TextView>(R.id.tvAmpluaCompare) //Амплуа
        private val imgPhoto: CircleImageView = itemView.findViewById(R.id.imgPhotoUserCompare)

        fun bind(item: Squad.Params){
            if(type_item == 0){
                //item user (left recycler)
                tvAmplua.visibility = View.GONE
                tvName.text = item.user_name
            }else{
                //item player (right recycler)
                imgPhoto.visibility = View.GONE
                tvName.text = item.player_name
                tvAmplua.text = item.amplua
            }
            itemView.setOnClickListener {
                callback.onItemClick(item, adapterPosition)
            }
        }
    }

    internal fun setList(list: MutableList<Squad.Params>){
        this.list = list
        notifyDataSetChanged()
    }
    interface Callback{
        fun onItemClick(item: Squad.Params, position: Int )
    }

}