package com.binduinfo.sports.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
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
import com.binduinfo.sports.data.model.address.AddressRequest
import com.binduinfo.sports.ui.activity.locationAdapter.AutoCompleteAdapter
import com.binduinfo.sports.ui.dialog.AlertDialogue
import com.binduinfo.sports.util.map.MapSupport.isServiceOk
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
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
import java.util.*

const val LOCATION_SETTINGS_REQUEST = 0x001
const val ADDRESS = "address"

class UserPlaceSelectActivity : BaseActivity(), OnMapReadyCallback,
    AdapterView.OnItemClickListener, GoogleMap.OnMarkerClickListener, AlertDialogue.AlertClickable {
    private lateinit var adapter: AutoCompleteAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var mMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var fusedLocationCallback: LocationCallback
    private val CAMERA_ZOOM = 15f
    private lateinit var locationRequest: LocationRequest
    private var isCurrentLocationSet = false
    private var addressStr: String = ""
    private var latitudeDou: Double = 0.0
    private var longitudeDou: Double = 0.0
    private var area: String = ""
    private var city: String = ""
    private var pincode: String = ""
    private var state: String = ""
    private var country: String = ""
    private lateinit var geoCoder: Geocoder
    private lateinit var address: List<Address>
    private lateinit var alertDialogue: AlertDialogue
    private var addressRequest: AddressRequest? = null
    override fun uiHandle() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
        setContentView(R.layout.activity_user_place_select)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        changeMyLocationButtonPosition(mapFragment.requireView())
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        alertDialogue =
            AlertDialogue(this, "Would we proceed with selected location ?", alertClick = this)
        placesClient = Places.createClient(this)
        initlizePlaceAutoComplete()
        if (isServiceOk(this, ERROR_DIALOG_REQUEST = 9001))
            permissionCheck()

        if (!::geoCoder.isInitialized) {
            geoCoder = Geocoder(this, Locale.getDefault())
        }
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
        mMap.setOnMarkerClickListener(this)
        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener {
            cameraMove(it, CAMERA_ZOOM, "Clicked Location", it.latitude, it.longitude)
        }
    }

    fun cameraMove(
        latLng: LatLng,
        zoom: Float,
        title: String,
        latitude: Double = 0.0,
        longtited: Double = 0.0
    ) {
        mMap.clear()
        val location = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap.animateCamera(location)
        val options: MarkerOptions = MarkerOptions().position(latLng).title(title)
            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_pin))
        mMap.addMarker(options)

        if (latitude > 0.0 && longtited > 0.0) {
            getAddressFromLatLong(latitude, longtited)
        }

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
                    if (it != null)
                        cameraMove(
                            it.place.latLng!!,
                            15f,
                            "",
                            it.place.latLng!!.latitude,
                            it.place.latLng!!.longitude
                        )
                    super.hideKeyBoard(this)
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
            }
        } else {

        }
    }

    private fun getLocation() {
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
                                "",
                                currentLocation.latitude,
                                currentLocation.longitude
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

        }
    }

    fun getAddressFromLatLong(latitude: Double, longtited: Double) {
        try {
            addressRequest = null
            address = geoCoder.getFromLocation(latitude, longtited, 1)
            if (address[0].getAddressLine(0) != null)
                addressStr = address[0].getAddressLine(0)
            if (address[0].subLocality != null)
                area = address[0].subLocality
            if (address[0].locality != null)
                city = address[0].locality
            if (address[0].adminArea != null)
                state = address[0].adminArea
            if (address[0].countryName != null)
                country = address[0].countryName
            if (address[0].postalCode != null)
                pincode = address[0].postalCode

            addressRequest =
                AddressRequest(addressStr, area, city, country, latitude, longtited, pincode, state)

        } catch (e: Exception) {

        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        alertDialogue.showdialogue()
        return false
    }

    override fun alertClickable() {
        val intent = Intent()
        intent.putExtra(ADDRESS, addressRequest)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

