package com.binduinfo.sports.util.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface NetworkInterFace {

    companion object {
        fun retrofitConnection(): Retrofit {


            return Retrofit.Builder()
                .baseUrl("bindhuinfotech.com/login")
                .addConverterFactory(GsonConverterFactory.create())//{"success": 1}
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }
}