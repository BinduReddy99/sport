package com.binduinfo.sports.ui.fragment.sportsrequest

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.ui.activity.UserPlaceSelectActivity
import com.binduinfo.sports.ui.bottomSheet.sportrequest.SportRequestListener

class SportsRequestListViewModel() : ViewModel(), SportsRequestListListener {
var listListener: SportsRequestListListener? = null




    fun bottomSheetClick(view: View){

        listListener?.showBottomSheet()
    }

    override fun showBottomSheet() {

    }

}