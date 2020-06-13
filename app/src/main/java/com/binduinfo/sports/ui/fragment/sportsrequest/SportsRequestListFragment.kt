package com.binduinfo.sports.ui.fragment.sportsrequest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.databinding.FragmentSportsRequestListBinding
import com.binduinfo.sports.ui.bottomSheet.sportrequest.SportsRequestBottomSheet
import com.binduinfo.sports.ui.fragment.signupfetchlocation.LOCATION_REQUEST_CODE
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber

class SportsRequestListFragment : BaseFragment(), KodeinAware, SportsRequestListListener {

    override val kodein by kodein()

    private lateinit var viewModel: SportsRequestListViewModel
    private lateinit var binding: FragmentSportsRequestListBinding

    private val factory: SportsRequestListFactory by instance<SportsRequestListFactory>()
    private val bottomSheet: SportsRequestBottomSheet by instance<SportsRequestBottomSheet>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, factory).get(SportsRequestListViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sports_request_list,
            container,
            false
        )

        binding.lifecycleOwner = this
        viewModel.listListener = this
        binding.viewModel = viewModel
        return binding.root//binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("", "")

    }

    override fun showBottomSheet() {
        if (!(bottomSheet.isAdded))
            bottomSheet.show(
                requireActivity().supportFragmentManager,
                SportsRequestListFragment::class.java.simpleName
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_REQUEST_CODE){
                Timber.d("====fragment${data.toString()}")
                showToast("====fragment${data.toString()}")
            }
        }
    }
}
