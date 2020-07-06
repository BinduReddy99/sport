@file:Suppress("UNREACHABLE_CODE")

package com.binduinfo.sports.ui.bottomSheet.sportrequest

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.R
import com.binduinfo.sports.databinding.BottomSheetSportRequestBinding
import com.binduinfo.sports.ui.activity.ADDRESS
import com.binduinfo.sports.ui.activity.UserPlaceSelectActivity
import com.binduinfo.sports.ui.activity.selectsport.SelectInterestedSportActivity
import com.binduinfo.sports.ui.fragment.signupfetchlocation.LOCATION_REQUEST_CODE
import com.binduinfo.sports.util.Constant
import com.binduinfo.sports.util.map.MapSupport
import com.binduinfo.sports.util.network.model.SportRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.bottom_sheet_sport_request.*
import kotlinx.android.synthetic.main.user_profile_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

const val ERROR_DIALOG_REQUEST = 9001

class SportsRequestBottomSheet : BottomSheetDialogFragment(), SportRequestListener, KodeinAware {
    override val kodein by kodein()

    private lateinit var binding: BottomSheetSportRequestBinding
    private lateinit var viewModel: SportRequestBottomViewModel
    private var format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private var timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
    private val now: Calendar? = Calendar.getInstance()
    private val selectedDate: Calendar? = Calendar.getInstance()
    private var date: String = ""
    private var time: String = ""
    var dateTime = "$date $time"

    private val factory: SportRequestBottomFactory by instance<SportRequestBottomFactory>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_sport_request, container, false)
        viewModel = ViewModelProvider(this, factory).get(SportRequestBottomViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.sportRequestListener = this


        // }

        return binding.root
    }

    private fun loadSportEventRequest() {
        viewModel.serverRequest.value = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        team_mate_number_picker.minValue = 1
        team_mate_number_picker.maxValue = 20
    }

    private fun checkLocationPermission() {
        if (MapSupport.isServiceOk(requireActivity(), ERROR_DIALOG_REQUEST))
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
                            Timber.d(intent.toString())
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                val address = data?.getParcelableExtra<SportRequest>(ADDRESS)
                if (address != null)
                    viewModel.address.value = address
            }
        }
    }

    private fun sportRequestNetwork() {

    }

    override fun cancel() {
    }

    override fun submit() {

    }

    override fun sportLocationFetch() {
        checkLocationPermission()
    }

    override fun selectSport() {
        val intent = Intent(requireContext(), SelectInterestedSportActivity::class.java)
        intent.putExtra(Constant.SELECT_SPORTS_KEY, Constant.REQUEST_SPORTS)
        startActivity(intent)


    }

    override fun selectDate() {
        val datePicker = now?.get(Calendar.YEAR)?.let {
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                    selectedDate?.set(Calendar.YEAR, year)
                    selectedDate?.set(Calendar.MONTH, month)
                    selectedDate?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = format.format(selectedDate?.time)

                    select_date_and_time.setText(date)

                },
                it,
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)

            )
        }

        if (datePicker != null) {
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        }
        datePicker?.show()


    }

    override fun selectEventTime() {

        now?.get(Calendar.HOUR_OF_DAY)?.let {
            TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    selectedDate?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedDate?.set(Calendar.MINUTE, minute)
                    time = timeFormat.format(selectedDate?.time)
                    if (selectedDate?.timeInMillis!! >= System.currentTimeMillis() - 1000) {
                        hourOfDay % 12
                        select_time_edit.setText(time)
                    } else {
                        Toast.makeText(requireContext(), "Invalid Time", Toast.LENGTH_LONG).show()
                    }


                },
                it, now.get(Calendar.MINUTE), false
            )
        }?.show()

    }


}

@BindingAdapter("location")
fun MapView.setLocation(location: List<Double>?) {
    mapView?.onCreate(Bundle())
    mapView?.getMapAsync { mMap -> // Add a marker
        mMap.uiSettings.setAllGesturesEnabled(false)
        if (location != null) {
            mapView?.onResume()
            val latitude = location[0]
            val longitude = location[1]
            mMap.clear()
            val location = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f)
            mMap.animateCamera(location)
            val options: MarkerOptions =
                MarkerOptions().position(LatLng(latitude, longitude)).title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            mMap.addMarker(options)
        }
    }
}
