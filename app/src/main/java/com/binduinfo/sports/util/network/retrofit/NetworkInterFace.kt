package com.binduinfo.sports.util.network.retrofit

import com.binduinfo.sports.util.network.model.GenerateOTP
import com.binduinfo.sports.util.network.model.ResponseOTP
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkInterFace {

    @POST("anonymous/generate-otp")
    fun requestOtp(@Body requestOTP: GenerateOTP): Observable<ResponseOTP>
    companion object {
        fun retrofitConnection(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://hosur-sports.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())//{"success": 1}
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }
}