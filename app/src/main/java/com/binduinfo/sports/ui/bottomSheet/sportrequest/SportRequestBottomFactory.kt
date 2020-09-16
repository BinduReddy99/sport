package com.binduinfo.sports.ui.bottomSheet.sportrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.data.repositores.SportsRequestRepository

class SportRequestBottomFactory(private val repository: SportsRequestRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SportRequestBottomViewModel(repository = repository) as T
    }
}