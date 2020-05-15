package com.binduinfo.sports.ui.fragment.selectinterestedsports

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.binduinfo.sports.util.network.model.Sport
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import io.reactivex.disposables.CompositeDisposable

class SportsDataSourceFactory(
    private val networkInterFace: NetworkInterFace,
    private val compositeDisposable: CompositeDisposable,
    private val type: String = "outdoor"
) : DataSource.Factory<Int, Sport>() {

    //val networkInterFace = NetworkInterFace.povideApi(NetworkInterFace.retrofitConnectionWithToken())

     val sportsDataSourceLiveData = MutableLiveData<SportsDataSource>()

    override fun create(): DataSource<Int, Sport> {
        val sportsDataSource =
            SportsDataSource(
                networkInterFace,
                compositeDisposable,
                type
            )
        sportsDataSourceLiveData.postValue(sportsDataSource)
        return sportsDataSource
    }
}