package com.binduinfo.sports.ui.fragment.selectinterestedsports.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.binduinfo.sports.util.enumpackage.State
import com.binduinfo.sports.util.network.model.Sport

class SportsListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<Sport, RecyclerView.ViewHolder>(SportDiffCalBack) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    companion object {
        val SportDiffCalBack = object :DiffUtil.ItemCallback<Sport>(){
            override fun areItemsTheSame(oldItem: Sport, newItem: Sport): Boolean {
               return oldItem._id === newItem._id
            }

            override fun areContentsTheSame(oldItem: Sport, newItem: Sport): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) SportsViewHolder.create(parent) else ListFooterViewHolder.create(retry, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("errrrrrrr---", position.toString())
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as SportsViewHolder).bind(getItem(position))
        else(holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("getItemViewType pos", position.toString())
        Log.d("getItemViewType pos", super.getItemCount().toString())
        return if(position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount pos", super.getItemCount().toString())
       // Log.d("getItemCount pos", super.getItemCount().toString())
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }
    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State){
        Log.d("setState pos", state.toString())
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

}