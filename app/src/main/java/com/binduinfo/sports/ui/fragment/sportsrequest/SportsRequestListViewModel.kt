package com.binduinfo.sports.ui.fragment.sportsrequest

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.ui.bottomSheet.sportrequest.SportRequestListener

class SportsRequestListViewModel(private val listener: SportsRequestListListener) : ViewModel(), SportRequestListener {
//loadTimingResponse
    override fun cancel() {

    }

    override fun submit() {
    }

    fun bottomSheetClick(view: View){

       listener.showBottomSheet()
    }

}