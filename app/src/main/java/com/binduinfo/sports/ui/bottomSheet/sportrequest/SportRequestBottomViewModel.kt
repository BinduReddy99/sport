package com.binduinfo.sports.ui.bottomSheet.sportrequest

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.repositores.SportsRequestRepository
import com.binduinfo.sports.util.network.model.SportRequest

class SportRequestBottomViewModel(private val repository: SportsRequestRepository) : ViewModel() {
    val serverRequest: MutableLiveData<Boolean> = MutableLiveData()
    val address: MutableLiveData<SportRequest> = MutableLiveData()
    var sportRequestListener: SportRequestListener? = null
    var tempSport: Boolean = false

    fun locate(view: View) {
        sportRequestListener?.sportLocationFetch()
    }

    fun selectOneSport(view: View) {
        sportRequestListener?.selectSport()

    }
    fun selectTime(view: View){
        sportRequestListener?.selectEventTime()
    }
    fun selectDate(view: View){
        sportRequestListener?.selectDate()
    }
}
