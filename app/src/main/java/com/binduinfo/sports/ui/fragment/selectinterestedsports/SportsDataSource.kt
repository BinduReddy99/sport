package com.binduinfo.sports.ui.fragment.selectinterestedsports

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.binduinfo.sports.util.enumpackage.State
import com.binduinfo.sports.util.network.model.Sport
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class SportsDataSource(
    private val networkInterface: NetworkInterFace,
    private val compositeDisposable: CompositeDisposable,
    private val type: String = "indoor"
) : PageKeyedDataSource<Int, Sport>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Sport>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(networkInterface.getSportsList(1, type).subscribe({
            updateState(State.DONE)
            callback.onResult(it.sports, null, 2)
            //callback.onResult(it.sports, 1, 2)
        }, {
            updateState(State.ERROR)
            setRetry(Action { loadInitial(params, callback) })

        }))


    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Sport>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkInterface.getSportsList(params.key, type).subscribe({
                updateState(State.DONE)
                callback.onResult(it.sports, params.key + 1)
            },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Sport>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
            )
        }
    }


    private fun setRetry(action: Action) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)

    }
}