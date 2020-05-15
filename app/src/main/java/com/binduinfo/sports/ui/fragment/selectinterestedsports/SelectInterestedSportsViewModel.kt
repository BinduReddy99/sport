package com.binduinfo.sports.ui.fragment.selectinterestedsports

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.binduinfo.sports.util.enumpackage.State
import com.binduinfo.sports.util.network.model.Sport
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.newSingleThreadContext

class SelectInterestedSportsViewModel : ViewModel() {

    private val networkInterFace =
        NetworkInterFace.povideApi(NetworkInterFace.retrofitConnectionWithToken())
    var sportList: LiveData<PagedList<Sport>>
    private val compositeDisposable = CompositeDisposable()
    private val sportsSourceFactory: SportsDataSourceFactory

    init {
        sportsSourceFactory = SportsDataSourceFactory(networkInterFace, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()

        sportList = LivePagedListBuilder(sportsSourceFactory, config).build()
    }

    fun getState():LiveData<State> = Transformations.switchMap<SportsDataSource, State>(sportsSourceFactory.sportsDataSourceLiveData, SportsDataSource::state)
    fun retry(){
        sportsSourceFactory.sportsDataSourceLiveData.value?.retry()
    }


    fun listIsEmpty(): Boolean{
        return sportList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
