package com.binduinfo.sports.ui.fragment.signupfetchlocation

import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.address.AddressRequest
import com.binduinfo.sports.data.repositores.LocationUpdateRepository
import com.example.mvvmsample.util.lazyDeferred
import kotlinx.coroutines.Deferred

class InstructLocationFetchViewModel(private val repository:LocationUpdateRepository) : ViewModel() {

    suspend fun updateLocation(addressRequest: AddressRequest): Lazy<Deferred<BasicModel>>{
       return lazyDeferred {
           repository.updateAddress(addressRequest = addressRequest)
       }
    }

}
