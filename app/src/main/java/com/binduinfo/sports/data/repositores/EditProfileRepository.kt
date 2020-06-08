package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.UpdateProfile
import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest
import com.binduinfo.sports.util.network.model.UpdateEditPersonalInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

class EditProfileRepository(private val api: MyApi): SafeAPIRequest() {

    suspend fun loadPersonalInfo(updatePersInfo: UpdateEditPersonalInfo):BasicModel{
        return withContext(IO){
            apiRequest {
                api.updatePersonalInfo(updatePersInfo)
            }
        }
    }
}


