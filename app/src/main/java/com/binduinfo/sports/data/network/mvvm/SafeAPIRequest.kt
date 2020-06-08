package com.binduinfo.sports.data.network.mvvm

import com.miziontrix.kmo.utils.exception.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response


abstract class SafeAPIRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val err = response.errorBody()?.toString()
            val message = StringBuilder()
            err?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {

                }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }
}