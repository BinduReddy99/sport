package com.binduinfo.sports.data.model

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.binduinfo.sports.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.hdodenhof.circleimageview.CircleImageView


data class ProfileInfo(
    val profile: Profile,
    val success: Int
)

@BindingAdapter("profilePic")
fun loadImage(imageView: CircleImageView, logo: String?){
    Glide.with(imageView.context.applicationContext).load(logo).apply(RequestOptions.circleCropTransform()).placeholder(ContextCompat.getDrawable(imageView.context, R.drawable.ic_use_white)).into(imageView)
}

@BindingAdapter("location")
    fun setLocation(mapView: MapView, location:List<Double>){
    if (mapView != null) {
        mapView.onCreate(Bundle())
        mapView.getMapAsync { googleMap -> // Add a marker
            googleMap.addMarker(MarkerOptions().position(LatLng(location[0], location[1])).title("Marker in India"))
        }
    }

}