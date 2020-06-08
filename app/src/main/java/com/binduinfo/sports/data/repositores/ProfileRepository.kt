package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.ProfileInfo
import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ProfileRepository(private val api: MyApi): SafeAPIRequest() {

    suspend fun imageUpload(profilePic: MultipartBody.Part): BasicModel{
        return  withContext(IO){
            apiRequest { api.uploadProfilePic(profilePic) }
        }
    }



    suspend fun loadInfo():ProfileInfo{
        return withContext(IO){
            apiRequest {  api.getUserInfo() }
        }
    }
}