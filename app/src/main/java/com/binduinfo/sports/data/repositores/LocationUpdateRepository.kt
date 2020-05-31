package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.address.AddressRequest
import com.example.mvvmsample.util.Coroutines
import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.SafeAPIRequest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class LocationUpdateRepository(private val api: MyApi): SafeAPIRequest(){

    suspend fun updateAddress(addressRequest: AddressRequest): BasicModel{
        return withContext(IO){
            apiRequest {
                api.updateAddress(addressRequest)
            }

        }
    }

}