package com.binduinfo.sports.ui.bottomSheet.sportrequest

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.repositores.SportsRequestRepository
import com.binduinfo.sports.ui.fragment.profile.ProfileHandler

class SportRequestBottomViewModel(private val repository: SportsRequestRepository): ViewModel() {
    val serverRequest: MutableLiveData<Boolean> = MutableLiveData()
    var sportRequestListener: SportRequestListener? = null

    fun locate(view: View){
        sportRequestListener?.sportLocationFetch()
    }

    fun selectOneSport(view: View){
    sportRequestListener?.selectSport()
    }
}