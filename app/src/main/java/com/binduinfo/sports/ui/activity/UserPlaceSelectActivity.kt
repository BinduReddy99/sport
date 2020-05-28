package com.binduinfo.sports.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.binduinfo.sports.R
import com.binduinfo.sports.base.BaseActivity
import com.binduinfo.sports.ui.fragment.signupfetchlocation.LOCATION_REQUEST_CODE
import com.binduinfo.sports.util.map.MapSupport.isServiceOk

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class UserPlaceSelectActivity : BaseActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    lateinit var mapFragment: SupportMapFragment
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    override fun uiHandle() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_place_select)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        if (isServiceOk(this, ERROR_DIALOG_REQUEST = 9001))
            permissionCheck()
        //mapFragment.getMapAsync(this)
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
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = this

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener {
            cameraMove(it, "Clicked Location")
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1,
            10f,
            locationListener
        )
//        val sydney = LatLng(12.7409, 77.8253)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Hosur"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun cameraMove(latLng: LatLng, title: String) {
        // val position = LatLng(12.7409, 77.8253)
        mMap.clear()
        val Zoom = 15f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoom))
        val options: MarkerOptions = MarkerOptions().position(latLng).title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap.addMarker(options)
    }

    override fun onLocationChanged(location: Location?) {
        location.let {
            cameraMove(LatLng(it?.latitude!!, it.longitude), "Current Location")
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
}
