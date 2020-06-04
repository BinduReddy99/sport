package com.binduinfo.sports.ui.fragment.editprofile

import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.R
import com.binduinfo.sports.data.model.About
import com.binduinfo.sports.data.model.UpdateProfile
import com.binduinfo.sports.data.repositores.EditProfileRepository
import timber.log.Timber


class ProfileEditViewModel(private val repository: EditProfileRepository) : ViewModel() {
    var updateProfile: MutableLiveData<UpdateProfile> = MutableLiveData()
        var aboutMe: MutableLiveData<About> = MutableLiveData()

    var editProfileHandler:EditProfileHandler? = null
    fun setData(updateProfile: UpdateProfile){
        this.updateProfile.value = updateProfile
        Log.d("updatpro", updateProfile.toString())
    }
    fun setAbout(about: About){
        this.aboutMe.value =about
    }
    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int) {
        //editProfileHandler?.radio()
        when(id){
            R.id.edit_gender_male_radio ->{
                updateProfile.value?.gender = "male"
            }
            R.id.edit_gender_female_radio ->{
                updateProfile.value?.gender = "female"
            }
            R.id.edit_gender_others_radio ->{
                updateProfile.value?.gender = "other"
            }
        }

        Timber.d("gender update ${updateProfile.value.toString()}")
    }

}