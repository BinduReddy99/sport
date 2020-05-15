package com.binduinfo.sports.ui.fragment.selectinterestedsports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.ui.fragment.selectinterestedsports.recyclerAdapter.SportsListAdapter
import com.binduinfo.sports.util.extension.hide
import com.binduinfo.sports.util.extension.show
import com.binduinfo.sports.util.network.model.Sport
import kotlinx.android.synthetic.main.select_interested_sports_fragment.*

class SelectInterestedSportsFragment : BaseFragment(), RecyleListFetchListener {

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
        viewModel.recyleListFetchListener = this
        return inflater.inflate(R.layout.select_interested_sports_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sports_list_progress_bar.show()
        viewModel.serverRequest()
    }

    override fun sports(sportsList: List<Sport>) {
        sportsAdapter = SportsListAdapter(sportsList, requireContext())
        sports_recycler_view.adapter = sportsAdapter
        sports_list_progress_bar.hide()
    }

    override fun throwable(throwable: Throwable) {
        sports_list_progress_bar.hide()
    }


}
