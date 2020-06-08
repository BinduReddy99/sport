package com.binduinfo.sports.ui.fragment.sportsrequest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.databinding.FragmentSportsRequestListBinding
import com.binduinfo.sports.ui.bottomSheet.sportrequest.SportsRequestBottomSheet
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext


class SportsRequestListFragment : BaseFragment(), KodeinAware, SportsRequestListListener {
    override val kodein by kodein()
   // private val context by instance<Context>

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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sports_request_list, container, false)

        binding.lifecycleOwner = this
        //viewModel.listListener = this
        binding.viewModel = viewModel
        return binding.root//binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("","")

    }
    override fun showBottomSheet() {
        Log.d("clickkkkk","clickkkkkk")

        Toast.makeText(requireContext(), "Hi", Toast.LENGTH_SHORT).show()
        //bottomSheet.show(requireActivity().supportFragmentManager, SportsRequestBottomSheet::class.java.simpleName)
    }
}
