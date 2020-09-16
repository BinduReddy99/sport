package com.binduinfo.sports.ui.fragment.sportsrequest

import android.view.View
import androidx.lifecycle.ViewModel

class SportsRequestListViewModel() : ViewModel(), SportsRequestListListener {
    var listListener: SportsRequestListListener? = null


    fun bottomSheetClick(view: View) {

        listListener?.showBottomSheet()
    }

    override fun showBottomSheet() {

    }

}