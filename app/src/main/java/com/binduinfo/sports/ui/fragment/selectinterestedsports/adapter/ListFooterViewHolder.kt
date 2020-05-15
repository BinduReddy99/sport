package com.binduinfo.sports.ui.fragment.selectinterestedsports.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binduinfo.sports.R
import com.binduinfo.sports.util.enumpackage.State
import com.binduinfo.sports.util.extension.hide
import kotlinx.android.synthetic.main.item_list_fotter.view.*

class ListFooterViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bind(status: State?){
        itemView.progress_bar.visibility = if (status == State.LOADING) VISIBLE else View.INVISIBLE
        itemView.text_error.visibility = if(status == State.ERROR) VISIBLE else View.INVISIBLE
    }

    companion object{
        fun create(retry:() -> Unit, parent: ViewGroup): ListFooterViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_fotter, parent, false)
            view.text_error.setOnClickListener {
                retry()
            }

            return ListFooterViewHolder(view)
        }
    }
}