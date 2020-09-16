package com.binduinfo.sports.ui.fragment.signupfetchlocation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseFragment
import com.binduinfo.sports.data.model.address.AddressRequest
import com.binduinfo.sports.data.preference.ADD_ADDRESS
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.ui.activity.ADDRESS
import com.binduinfo.sports.ui.activity.HomeActivity
import com.binduinfo.sports.ui.activity.UserPlaceSelectActivity
import com.binduinfo.sports.util.map.MapSupport.isServiceOk
import com.example.mvvmsample.util.Coroutines
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.instruct_location_fetch_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

const val LOCATION_REQUEST_CODE = 0x001
private val ERROR_DIALOG_REQUEST = 9001

class InstructLocationFetch : BaseFragment(), KodeinAware {
    override val kodein by kodein()
    private val preference: PreferenceProvider by instance<PreferenceProvider>()
    private val factory: InstructLocationFetchViewModelFactory by instance<InstructLocationFetchViewModelFactory>()
    private lateinit var viewModel: InstructLocationFetchViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(InstructLocationFetchViewModel::class.java)
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
                            val intent =
                                Intent(requireContext(), UserPlaceSelectActivity::class.java)
                            requireActivity().startActivityForResult(intent, LOCATION_REQUEST_CODE)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Enable Permission",
                                Toast.LENGTH_SHORT
                            ).show()
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

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("err====", requestCode.toString())
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                Log.d("err====", requestCode.toString())
                val address = data?.getParcelableExtra<AddressRequest>(ADDRESS)
                if (address is AddressRequest) {
                    address_load_progress.visibility = View.VISIBLE
                    get_my_location.visibility = View.GONE
                    Coroutines.main {
                        viewModel.updateLocation(address).value.await().run {
                            if (success == 1) {
                                preference?.storeValue(
                                    ADD_ADDRESS, true
                                )
                                val intent = Intent(requireContext(), HomeActivity::class.java)
                                intent.let {
                                    it.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    requireActivity().startActivity(it)
                                }
                            } else {
                                showToast(message)
                            }
                            address_load_progress.visibility = View.GONE
                            get_my_location.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }


}
