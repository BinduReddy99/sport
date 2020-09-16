package com.binduinfo.sports.ui.fragment.sportsrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SportsRequestListFactory() : ViewModelProvider.NewInstanceFactory() {
    //listener = listener
    //private val listener: SportsRequestListListener
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SportsRequestListViewModel() as T
    }
}