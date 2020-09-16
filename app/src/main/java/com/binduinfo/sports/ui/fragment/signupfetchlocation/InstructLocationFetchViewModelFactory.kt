package com.binduinfo.sports.ui.fragment.signupfetchlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.data.repositores.LocationUpdateRepository

class InstructLocationFetchViewModelFactory(private val repository: LocationUpdateRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InstructLocationFetchViewModel(repository = repository) as T
    }
}