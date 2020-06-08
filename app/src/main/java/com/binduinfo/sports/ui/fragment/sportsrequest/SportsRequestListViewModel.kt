package com.binduinfo.sports.ui.fragment.sportsrequest

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.ui.bottomSheet.sportrequest.SportRequestListener

class SportsRequestListViewModel() : ViewModel(), SportRequestListener {
var listListener: SportsRequestListListener? = null

    override fun cancel() {

    }

    override fun submit() {
    }

    fun bottomSheetClick(view: View){

        listListener?.showBottomSheet()
    }

}