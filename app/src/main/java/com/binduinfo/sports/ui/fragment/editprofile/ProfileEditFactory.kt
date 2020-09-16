package com.binduinfo.sports.ui.fragment.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.data.repositores.EditProfileRepository

class ProfileEditFactory(private val repository: EditProfileRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileEditViewModel(repository = repository) as T
    }
}