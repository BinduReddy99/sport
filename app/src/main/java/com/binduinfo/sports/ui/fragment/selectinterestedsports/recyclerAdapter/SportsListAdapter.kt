package com.binduinfo.sports.ui.fragment.selectinterestedsports.recyclerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binduinfo.sports.R
import com.binduinfo.sports.util.network.model.Sport
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.sport_user_select_item.view.*

class SportsListAdapter(private  val sportsList: List<Sport>, private val context: Context): RecyclerView.Adapter<SportsListAdapter.SportsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SportsHolder(layoutInflater.inflate(R.layout.sport_user_select_item, parent, false))
    }

    override fun getItemCount() = sportsList.size

    override fun onBindViewHolder(holder: SportsHolder, position: Int) {
        val sport = sportsList[position]

        sport.run {
            holder.gameName.text = name
                Glide.with(context).load(imagePath).into(holder.gameImageView)
        }
        holder.rootClick.setOnClickListener {
            sportsList[position].isSeleted = !sportsList[position].isSeleted
            holder.checkImageView.visibility = if (sportsList[position].isSeleted) View.VISIBLE else View.GONE
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class SportsHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val gameImageView = itemView.game_image_view
        val gameName = itemView.game_name
        val checkImageView = itemView.check_item
        val rootClick = itemView.rootView

    }


}