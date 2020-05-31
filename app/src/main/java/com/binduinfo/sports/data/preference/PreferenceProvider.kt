package com.binduinfo.sports.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

const val LOGIN_TOKEN = "login_token"
const val IS_LOGGED_IN = "is_logged_in"
const val ADD_ADDRESS = "is_address_added"
const val ADD_INTERESTED_SPORT = "is_interested_sport_added"
class PreferenceProvider(private val context: Context) {
    private val appContext = context.applicationContext

    private val preference: SharedPreferences get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun storeValue(key: String, value: String){
        preference.edit().putString(key, value).apply()
    }

    fun storeValue(key: String, value: Boolean){
        preference.edit().putBoolean(key, value).apply()
    }

    fun getsharedBoolean(key: String): Boolean = preference.getBoolean(key, false)

    fun getSharedString(key: String): String = preference.getString(key, "")!!


}