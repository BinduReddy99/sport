package com.binduinfo.sports.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.ui.activity.locationAdapter.AutoCompleteAdapter
import com.binduinfo.sports.util.map.MapSupport.isServiceOk
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_user_place_select.*
import java.lang.Exception

class UserPlaceSelectActivity : BaseActivity(), OnMapReadyCallback,
    AdapterView.OnItemClickListener {
    private lateinit var adapter: AutoCompleteAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var mMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var fusedLocationCallback: LocationCallback
    private val CAMERA_ZOOM = 15f
    override fun uiHandle() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_place_select)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        changeMyLocationButtonPosition(mapFragment.requireView())
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)
        initlizePlaceAutoComplete()
        if (isServiceOk(this, ERROR_DIALOG_REQUEST = 9001))
            permissionCheck()
    }

    private fun initlizePlaceAutoComplete() {
        input_search.threshold = 3
        adapter = AutoCompleteAdapter(this, placesClient)
        input_search.onItemClickListener = this
        input_search.setAdapter(adapter)
    }


    private fun permissionCheck() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if (it.areAllPermissionsGranted()) {
                        if (::mapFragment.isInitialized)
                            mapFragment.getMapAsync(this@UserPlaceSelectActivity)
                        else
                            showToast("Something went wrong")
                        getMyLocation()

                    } else {
                        Toast.makeText(
                            this@UserPlaceSelectActivity,
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener {
            cameraMove(it, CAMERA_ZOOM, "Clicked Location")
        }
    }

    fun cameraMove(latLng: LatLng, zoom: Float, title: String) {
        mMap.clear()
        val location = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap.animateCamera(location)
        val options: MarkerOptions = MarkerOptions().position(latLng).title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap.addMarker(options)

    }

    private fun getMyLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest: LocationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2000
        locationRequest.fastestInterval = 2000
        locationRequest.smallestDisplacement = 10f
        try {
            fusedLocationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    cameraMove(
                        LatLng(p0?.lastLocation!!.latitude, p0.lastLocation!!.longitude),
                        15f,
                        ""
                    )
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    Log.d("location Avaability====", p0.toString())
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                fusedLocationCallback,
                Looper.getMainLooper()
            )

        } catch (e: SecurityException) {
            // Log.e(TAG, "getDeviceLocation: SecurityException" + e.message)
        }
    }

    private fun changeMyLocationButtonPosition(mapView: View) {
        if (mapView?.findViewById<View>(Integer.parseInt("1")) != null
        ) {
            val locationButton: View =
                (mapView.findViewById<View>("1".toInt())
                    .parent as View).findViewById("2".toInt())
            // and next place it, on bottom right (as Google Maps app)
            // and next place it, on bottom right (as Google Maps app)
            val layoutParams: RelativeLayout.LayoutParams =
                locationButton.layoutParams as RelativeLayout.LayoutParams
            // position on right bottom
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 30, 30)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        try {
            val item = adapter.getItem(position)
            var placeID = ""
            if (item != null)
                placeID = item.placeId

            val plaFields = arrayListOf<Place.Field>(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            var req:FetchPlaceRequest? = null

            if (placeID != null){
                req = FetchPlaceRequest.builder(placeID, plaFields).build()
            }

            if(req != null){
                placesClient.fetchPlace(req).addOnSuccessListener ({

                }, {
                    cameraMove(it.place.latLng!!, CAMERA_ZOOM, "")
                })
            }



        } catch (e: Exception) {

        }
    }
}
