package com.binduinfo.sports.app

import android.app.Application
import android.util.Log
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.data.repositores.SportsRepository
import com.binduinfo.sports.ui.fragment.profile.ProfileFragment
import com.binduinfo.sports.ui.fragment.profile.ProfileHandler
import com.binduinfo.sports.ui.fragment.selectinterestedsports.SelectInterestedSportsViewModelFactory
import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.NetworkConnectionInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class BaseApplication() : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))
        bind() from singleton { PreferenceProvider(instance())}
        bind() from singleton { NetworkConnectionInterceptor(instance(), instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDataBase(instance()) }
        bind() from  provider { SportsRepository(instance(), instance()) }
        bind() from provider { SelectInterestedSportsViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
    }

}