package com.binduinfo.sports.ui.fragment.editprofile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.model.UpdateProfile
import com.binduinfo.sports.data.repositores.EditProfileRepository

class ProfileEditViewModel(private val repository: EditProfileRepository) : ViewModel() {
    val updateProfile: MutableLiveData<UpdateProfile> = MutableLiveData()

    fun setData(updateProfile: UpdateProfile){
        this.updateProfile.value = updateProfile
        Log.d("updatpro", updateProfile.toString())
    }

}