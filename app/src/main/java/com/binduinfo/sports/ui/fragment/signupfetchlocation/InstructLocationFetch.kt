package com.binduinfo.sports.ui.fragment.signupfetchlocation

import android.Manifest
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import com.binduinfo.sports.R
import com.binduinfo.sports.ui.activity.UserPlaceSelectActivity
import com.binduinfo.sports.util.map.MapSupport.isServiceOk
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.instruct_location_fetch_fragment.*
const val LOCATION_REQUEST_CODE = 0x001
private val ERROR_DIALOG_REQUEST = 9001
class InstructLocationFetch : Fragment() {

    companion object {
        fun newInstance() = InstructLocationFetch()
    }

    private lateinit var viewModel: InstructLocationFetchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.instruct_location_fetch_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        get_my_location.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (isServiceOk(requireActivity(), ERROR_DIALOG_REQUEST))
            Dexter.withActivity(requireActivity()).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
            ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (it.areAllPermissionsGranted()) {
                        val intent = Intent(requireContext(), UserPlaceSelectActivity::class.java)
                        requireActivity().startActivityForResult(intent, LOCATION_REQUEST_CODE)
                    }else{
                        Toast.makeText(requireContext(), "Enable Permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.let {
                    token.continuePermissionRequest()
                }
            }

        }).check()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InstructLocationFetchViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


}
