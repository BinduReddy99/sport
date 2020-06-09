package com.binduinfo.sports.ui.activity.selectsport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.repositores.SportsRepository

@Suppress("UNCHECKED_CAST")

class SelectInterestedSportsViewModelFactory (private val repository: SportsRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SelectInterestedSportsViewModel(repository = repository) as T
    }
}