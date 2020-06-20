package com.binduinfo.sports.ui.fragment.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.model.*
import com.binduinfo.sports.data.repositores.ProfileRepository
import com.binduinfo.sports.util.imagecompessorsupportmodule.Compressor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.user_profile_layout.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    val progMutable: MutableLiveData<Boolean> = MutableLiveData()
    val serverRequest: MutableLiveData<Boolean> = MutableLiveData()
    var profileHandler: ProfileHandler? = null
    var profileInfo: MutableLiveData<ProfileInfo> =  MutableLiveData<ProfileInfo>().apply {
        CoroutineScope(Main).launch {
           value = repository.loadInfo()



        }
    }
    fun imageSelect(view: View){
        profileHandler?.profilePic()
    }

    fun uploadProfileInfo(view: View, profile: Profile){
        profile.run {
            profileHandler?.updateProfileInfo(UpdateProfile(email, gender, mobileNumber, name))
        }

    }

    fun editLocation(view: View){
        profileHandler?.profileLocationEdit()
    //edit_location.setOnClickListener()
    }


    fun editSelectedSport(view: View){
        profileHandler?.selectSport()
    }

    fun logout(view: View){
        profileHandler?.logout()
    }

    suspend fun uploadImage(context: Context, uri: Uri): BasicModel{
        progMutable.value = true
        return withContext(IO) {
            val compressFile = async {
                imageCompressing(context, uri)
            }.await()

           withContext(IO) {
                imageUploadToServer(compressFile)
            }

        }

    }


    suspend fun imageUploadToServer(compressFile: File): BasicModel{
        return withContext(IO){
            Log.d("image ====", compressFile.toString())
           val multiPart =  MultipartBody.Part.createFormData(
                "image", compressFile.name, compressFile
                    .asRequestBody("image/*".toMediaTypeOrNull()))
            repository.imageUpload(multiPart)
        }
    }

    suspend fun imageCompressing(context: Context, uri: Uri): File{
        return withContext(IO){
            var file = File(uri.path)
            Compressor(context, Compressor.PROFILE_PIC)
                .compressToFile(file)
        }
    }

}

@BindingAdapter("location")
fun MapView.setLocation( location:List<Double>?){
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
            val options: MarkerOptions = MarkerOptions().position(LatLng(latitude, longitude)).title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            mMap.addMarker(options)
        }
    }

}