package com.binduinfo.sports.ui.bottomSheet.sportrequest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.R
import com.binduinfo.sports.databinding.BottomSheetSportRequestBinding
import com.binduinfo.sports.databinding.FragmentProfileBinding
import com.binduinfo.sports.ui.fragment.profile.ProfileViewModel
import com.binduinfo.sports.ui.fragment.profile.ProfileViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_sport_request.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SportsRequestBottomSheet(): BottomSheetDialogFragment(), KodeinAware {
    override val kodein by kodein()

    private lateinit var binding: BottomSheetSportRequestBinding
    private lateinit var viewModel: SportRequestBottomViewModel
    private val factory: SportRequestBottomFactory by instance<SportRequestBottomFactory>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_sport_request, container, false)
        viewModel =  ViewModelProvider(this, factory).get(SportRequestBottomViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        team_mate_number_picker.minValue = 1
        team_mate_number_picker.maxValue = 20
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}