package com.binduinfo.sports.app

import android.app.Application
import android.util.Log
import com.binduinfo.sports.util.preference.PreferenceProvider

class BaseApplication : Application() {

    private val sharedPreferences: PreferenceProvider? = null

    override fun onCreate() {
        super.onCreate()
        if(instance == null)
        instance = this
    }

    companion object {
         var instance: BaseApplication? = null
    }

    /**
     * Singleton Object
     */
    fun getSharedPreferenceObj(): PreferenceProvider? {
        return if (sharedPreferences == null) {
            Log.d("Application Obj", "new")
            PreferenceProvider(this)
        } else {
            Log.d("Application Obj", "old")
            sharedPreferences!!
        }
    }

}