package com.binduinfo.sports.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.ui.activity.locationAdapter.AutoCompleteAdapter
import com.binduinfo.sports.util.map.MapSupport.isServiceOk
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
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

const val LOCATION_SETTINGS_REQUEST = 0x001

class UserPlaceSelectActivity : BaseActivity(), OnMapReadyCallback,
    AdapterView.OnItemClickListener {
    private lateinit var adapter: AutoCompleteAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var mMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var fusedLocationCallback: LocationCallback
    private val CAMERA_ZOOM = 15f
    private lateinit var locationRequest: LocationRequest
    private var isCurrentLocationSet = false
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
        adapter = AutoCompleteAdapter(this, R.layout.auto_complete_place_picker_item, placesClient)
        input_search.onItemClickListener = this
        input_search.dropDownVerticalOffset = 0
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
            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_pin))
        mMap.addMarker(options)

    }

    private fun getMyLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val googleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API).build()
        googleApiClient.connect()

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2000
        locationRequest.fastestInterval = 2000
        locationRequest.smallestDisplacement = 2000f

        val builder: LocationSettingsRequest.Builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        result.addOnCompleteListener { p0 ->
            try {
                val response = p0.getResult(ApiException::class.java)
            } catch (ex: ApiException) {

            }
        }.addOnFailureListener { p0 ->
            val status = p0
            val ex = (p0 as ApiException).statusCode
            when (ex) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvableApiException: ResolvableApiException =
                            status as ResolvableApiException
                        resolvableApiException
                            .startResolutionForResult(
                                this@UserPlaceSelectActivity,
                                LOCATION_SETTINGS_REQUEST
                            )
                    } catch (e: IntentSender.SendIntentException) {

                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                }
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.d("Sucesss=====", "Sucesss")

                }
            }
        }.addOnCanceledListener {

        }
        getLocation()
    }

    private fun changeMyLocationButtonPosition(mapView: View) {
        if (mapView.findViewById<View>(Integer.parseInt("1")) != null
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

            val plaFields =
                arrayListOf<Place.Field>(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            var req: FetchPlaceRequest? = null

            if (placeID != null) {
                req = FetchPlaceRequest.builder(placeID, plaFields).build()
            }

            if (req != null) {
                placesClient.fetchPlace(req).addOnSuccessListener {
                    cameraMove(it.place.latLng!!, 15f, "")
                }.addOnFailureListener { }
            }
        } catch (e: Exception) {

        }

    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_SETTINGS_REQUEST) {
                Log.d("sucess requet", "success request")
              //  getLocation()
            }
        } else {

        }
    }

    private fun getLocation(){
        try {
            fusedLocationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    val currentLocation = p0?.lastLocation
                    if (currentLocation != null) {
                        if (!isCurrentLocationSet) {
                            isCurrentLocationSet = true
                            cameraMove(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                15f,
                                ""
                            )
                        }
                    }
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
}

//   val location = fusedLocationProviderClient.lastLocation

//            location.addOnCompleteListener {
//                if (it.isSuccessful) {
//                    if(location != null) {
//                        val currentLocation = it.result as Location
//                        cameraMove(
//                            LatLng(currentLocation.latitude, currentLocation.longitude),
//                            15f,
//                            ""
//                        )
//                    }
//                } else {
//                    Log.d("TAG", "onComplete: current location is null")
//                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_LONG)
//                        .show()
//                }
//            }
