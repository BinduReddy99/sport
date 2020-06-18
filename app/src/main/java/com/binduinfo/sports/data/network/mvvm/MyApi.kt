package com.binduinfo.sports.data.network.mvvm


import com.binduinfo.sports.BuildConfig
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.ProfileInfo
import com.binduinfo.sports.data.model.address.AddressRequest
import com.binduinfo.sports.data.model.sport.RequestInterestSport
import com.binduinfo.sports.util.network.model.SportRequestEventResponse
import com.binduinfo.sports.util.network.model.SportsListResponse
import com.binduinfo.sports.util.network.model.UpdateEditPersonalInfo
import com.miziontrix.kmo.data.network.api.mvvm.NetworkConnectionInterceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

const val BASE_URL: String = "https://ssport.herokuapp.com/api/"

interface MyApi {
    @GET("user/sport")
    suspend fun getSportsList(): Response<SportsListResponse>

    @Multipart
    @PUT("user/profile-pic")
    suspend fun uploadProfilePic(@Part file: MultipartBody.Part?): Response<BasicModel>

    @GET("user/profile")
    suspend fun getUserInfo(): Response<ProfileInfo>

    @PUT("user/interested")
    suspend fun updateInterestedSport(@Body sportSelectRequest: RequestInterestSport): Response<BasicModel>

    @PUT("user/address")
    suspend fun updateAddress(@Body addressRequest: AddressRequest): Response<BasicModel>

    @PUT("user/profile")
    suspend fun updatePersonalInfo(@Body updateEditPersonalInfo: UpdateEditPersonalInfo):Response<BasicModel>

    @GET("user/request-sport")
    suspend fun requestSportEvent():Response<SportRequestEventResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(logsInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
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