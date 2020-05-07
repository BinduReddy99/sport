package com.binduinfo.sports.util.network.retrofit

import com.binduinfo.sports.BuildConfig
import com.binduinfo.sports.util.network.model.GenerateOTP
import com.binduinfo.sports.util.network.model.ResponseOTP
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface NetworkInterFace {

    @POST("anonymous/generate-otp")
    fun requestOtp(@Body requestOTP: GenerateOTP): Observable<ResponseOTP>


    companion object {
        fun retrofitConnection(): Retrofit {

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(logsInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://hosur-sports.herokuapp.com/api/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())//{"success": 1}
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }


        fun logsInterceptor(): HttpLoggingInterceptor {
            val logInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logInterceptor.level = HttpLoggingInterceptor.Level.NONE

            }

            return logInterceptor
        }
    }
}