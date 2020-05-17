package com.binduinfo.sports.ui.fragment.selectinterestedsports.recyclerAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.binduinfo.sports.R
import com.binduinfo.sports.util.network.model.Sport
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.sport_user_select_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class SportsListAdapter(private  var sportsList: List<Sport>, private val context: Context, private val clickable: ItemClickable): RecyclerView.Adapter<SportsListAdapter.SportsHolder>(),
    Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SportsHolder(layoutInflater.inflate(R.layout.sport_user_select_item, parent, false))
    }
    var sportFilterList: List<Sport> = arrayListOf<Sport>()
    init {
        sportFilterList = sportsList
    }
    override fun getItemCount() = sportFilterList.size

    override fun onBindViewHolder(holder: SportsHolder, position: Int) {
        val sport = sportFilterList[position]

        sport.run {
            holder.gameName.text = name
                //Glide.with(context).load(imagePath).into(holder.gameImageView)
            if(isSeleted){
                holder.gameName.background = ContextCompat.getDrawable(context, R.drawable.sport_select_bg)
                holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.white))
            }else{
                holder.gameName.background = ContextCompat.getDrawable(context, R.drawable.sport_unselect_bg)
                holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            }
            holder.rootClick.setOnClickListener {
                isSeleted = !isSeleted
                if(isSeleted){
                    holder.gameName.background = ContextCompat.getDrawable(context, R.drawable.sport_select_bg)
                    holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.white))
                }else{
                    holder.gameName.background = ContextCompat.getDrawable(context, R.drawable.sport_unselect_bg)
                    holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                }
                clickable.updateItem(_id, isSeleted)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class SportsHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val gameName = itemView.game_name
        val rootClick = itemView.rootView
    }

    fun updateList(list: List<Sport>){
        sportFilterList = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchText = constraint.toString()
                if (searchText.isEmpty()){
                    sportFilterList = sportsList
                }else{
                    val resultList = ArrayList<Sport>()
                    for (row in sportsList) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    sportFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = sportFilterList
                return filterResults
                }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                sportFilterList = results?.values as ArrayList<Sport>
                notifyDataSetChanged()            }
        }
    }

    interface ItemClickable{
        fun updateItem(_id: String, isSelect: Boolean)
    }

}