package com.binduinfo.sports.util.network.retrofit

import android.util.Base64
import com.binduinfo.sports.BuildConfig
import com.binduinfo.sports.app.BaseApplication
import com.binduinfo.sports.data.network.mvvm.MyApi.Companion.logsInterceptor
import com.binduinfo.sports.util.network.model.*
import com.binduinfo.sports.data.preference.LOGIN_TOKEN
import com.binduinfo.sports.data.preference.PreferenceProvider
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

const val BASE_URL: String = "https://ssport.herokuapp.com/api/"

interface NetworkInterFace {

    @POST("anonymous/generate-otp")
    fun requestOtp(@Body requestOTP: GenerateOTP): Observable<SportResponse>

    @POST("anonymous/sign-up")
    fun signUp(@Body signUpRequest: SignUpRequest): Observable<SportResponse>

    @POST("anonymous/login")
    fun signIn(): Observable<LoginResponse>

    @POST("user/sports-request")
    fun requestSportEvent(@Body sportRequest: SportRequest):Observable<SportRequestEventResponse>

    @GET("user/sport/{page}/{type}")
    fun getSportsList(@Path(value = "page") page: Int, @Path(value = "type") type: String): Observable<SportsListResponse>


    companion object {
        fun retrofitConnection(): Retrofit {

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(logsInterceptor())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())//{"success": 1}
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        fun retrofitConnection(userName: String, password: String): Retrofit {
            val credinational = "$userName:$password"
            val basic =
                "Basic " + Base64.encodeToString(credinational.toByteArray(), Base64.NO_WRAP)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().addHeader("Authorization", basic)
                    .method(original.method, original.body)
                chain.proceed(builder.build())
            }.addInterceptor(logsInterceptor())
                .addInterceptor(logsInterceptor())
                .readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        // fun retrofitConnection(){
//        val httpClient = OkHttpClient.Builder()
//            .addInterceptor(logsInterceptor())
//            .readTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .build()
//
//        return Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(httpClient)
//        .addConverterFactory(GsonConverterFactory.create())//{"success": 1}
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .build()
//    }

        fun retrofitConnectionWithToken(preferenceProvider: PreferenceProvider) : Retrofit{
            val token: String? = preferenceProvider.getSharedString(LOGIN_TOKEN)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .method(original.method, original.body)
                chain.proceed(builder.build())
            }.addInterceptor(logsInterceptor())
                .addInterceptor(logsInterceptor())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        fun povideApi(retrofit: Retrofit): NetworkInterFace {
            return retrofit.create(NetworkInterFace::class.java)
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