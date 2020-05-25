package com.binduinfo.sports.ui.fragment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.data.repositores.ProfileRepository

class ProfileViewModelFactory(private val repository: ProfileRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(repository = repository) as T
    }
}