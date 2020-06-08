package com.binduinfo.sports.app

import android.app.Application
import com.binduinfo.sports.BuildConfig
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.preference.PreferenceProvider
import com.binduinfo.sports.data.repositores.*
import com.binduinfo.sports.ui.bottomSheet.sportrequest.SportRequestListener
import com.binduinfo.sports.ui.bottomSheet.sportrequest.SportsRequestBottomSheet
import com.binduinfo.sports.ui.fragment.editprofile.ProfileEditFactory
import com.binduinfo.sports.ui.fragment.profile.ProfileViewModelFactory
import com.binduinfo.sports.ui.fragment.selectinterestedsports.SelectInterestedSportsViewModelFactory
import com.binduinfo.sports.ui.fragment.signupfetchlocation.InstructLocationFetchViewModelFactory
import com.binduinfo.sports.ui.fragment.sportsrequest.SportsRequestListFactory
import com.binduinfo.sports.ui.fragment.sportsrequest.SportsRequestListFragment
import com.binduinfo.sports.ui.fragment.sportsrequest.SportsRequestListListener
import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.NetworkConnectionInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class BaseApplication() : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance(), instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDataBase(instance()) }
        bind() from provider { SportsRepository(instance(), instance()) }
        bind() from provider { SelectInterestedSportsViewModelFactory(instance()) }
        bind() from provider { ProfileRepository(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from provider { LocationUpdateRepository(instance()) }
        bind() from provider { InstructLocationFetchViewModelFactory(instance()) }
        bind() from provider { EditProfileRepository(instance()) }
        bind() from provider { ProfileEditFactory(instance()) }

        /**
         * Sports post
         */
      //  bind() from provider { SportsRequestRepository(instance()) }
        //bind() from singleton { SportsRequestBottomSheet() }
        bind<SportsRequestListListener>() with   singleton { SportsRequestListFragment() }
       // bind() from singleton { SportsRequestListFragment() }

        bind() from provider { SportsRequestListFactory(instance()) }


    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}