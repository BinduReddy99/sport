package com.binduinfo.sports.ui.fragment.selectinterestedsports.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binduinfo.sports.R
import com.binduinfo.sports.util.network.model.Sport
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.sport_user_select_item.view.*

class SportsViewHolder(private val view: View): RecyclerView.ViewHolder(view){
    fun bind(
        games: Sport?,
        position: Int,
        setSelectItem:(position:Int) -> Unit
    ){

        Log.d("games ===== ", "gamess=== ${position}")
        if (games != null){

            itemView.root_clickable.setOnClickListener {
                Log.d("position ==========", games._id)
                if (games._id == games._id) {
                    games.isSeleted = !games.isSeleted
                    itemView.check_item.visibility =
                        if (games.isSeleted) View.VISIBLE else View.GONE
                }
            }

            itemView.game_name.text = games.name
            Glide.with(view).load(games.imagePath).into(itemView.game_image_view)
        }
    }


    companion object{
        fun create(parent: ViewGroup): SportsViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sport_user_select_item, parent, false)
            return SportsViewHolder(view)
        }
    }
}