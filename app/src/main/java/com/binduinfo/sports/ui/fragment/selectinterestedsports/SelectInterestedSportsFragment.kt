package com.binduinfo.sports.ui.fragment.selectinterestedsports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.binduinfo.sports.R
import com.binduinfo.sports.ui.fragment.selectinterestedsports.adapter.SportsListAdapter
import com.binduinfo.sports.util.enumpackage.State
import kotlinx.android.synthetic.main.select_interested_sports_fragment.*

class SelectInterestedSportsFragment : Fragment() {

    companion object {
        fun newInstance() =
            SelectInterestedSportsFragment()
    }

    private lateinit var viewModel: SelectInterestedSportsViewModel
    private lateinit var sportsAdapter: SportsListAdapter
    private lateinit var mLayoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SelectInterestedSportsViewModel::class.java)
        return inflater.inflate(R.layout.select_interested_sports_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initstate()
    }

    private fun initstate() {
        text_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(viewLifecycleOwner, Observer {state ->
            sports_list_progress_bar.visibility =  if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            text_error.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                sportsAdapter.setState(state ?: State.DONE)
            }
            }
        )
    }

    private fun initAdapter() {
//        mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
//        sports_recycler_view.layoutManager = mLayoutManager
        sportsAdapter = SportsListAdapter { viewModel.retry() }
        sports_recycler_view.adapter = sportsAdapter
        viewModel.sportList.observe(viewLifecycleOwner, Observer {
            sportsAdapter.submitList(it)
        })
    }


}
