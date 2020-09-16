package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.model.About
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest
import com.binduinfo.sports.util.network.model.UpdateEditPersonalInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class EditProfileRepository(private val api: MyApi) : SafeAPIRequest() {

    suspend fun loadPersonalInfo(updateEditPersonalInfo: UpdateEditPersonalInfo): BasicModel {
        return withContext(IO) {
            apiRequest {
                api.updatePersonalInfo(updateEditPersonalInfo)
            }
        }
    }

    suspend fun editAbout(updateAbout: About): BasicModel {
        return withContext(IO) {
            apiRequest {
                api.updateAbout(updateAbout)
            }
        }
    }
}


