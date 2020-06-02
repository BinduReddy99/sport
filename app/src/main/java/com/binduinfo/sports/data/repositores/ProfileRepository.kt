package com.binduinfo.sports.data.repositores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.ProfileInfo
import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.SafeAPIRequest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.internal.addHeaderLenient
import retrofit2.http.Multipart

class ProfileRepository(private val api: MyApi): SafeAPIRequest() {

    suspend fun imageUpload(profilePic: MultipartBody.Part): BasicModel{
        return  withContext(IO){
            apiRequest { api.uploadProfilePic(profilePic) }
        }
    }

//    suspend fun personalInfoUpload():{
//        return withContext(IO){
//            apiRequest { api.updateAddress() }
//        }
//    }

    suspend fun loadInfo():ProfileInfo{
        return withContext(IO){
            apiRequest {  api.getUserInfo() }
        }
    }
}