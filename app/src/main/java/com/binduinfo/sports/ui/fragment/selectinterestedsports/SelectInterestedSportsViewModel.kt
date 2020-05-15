package com.binduinfo.sports.ui.fragment.selectinterestedsports

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.binduinfo.sports.util.enumpackage.State
import com.binduinfo.sports.util.network.model.Sport
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.newSingleThreadContext

class SelectInterestedSportsViewModel : ViewModel() {
    var recyleListFetchListener: RecyleListFetchListener? = null
    private val networkInterFace =
        NetworkInterFace.povideApi(NetworkInterFace.retrofitConnectionWithToken())

    @SuppressLint("CheckResult")
    fun serverRequest(){
        networkInterFace.getSportsList(1, "all").observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                recyleListFetchListener?.sports(it.sports)
            },{
                recyleListFetchListener?.throwable(it)
            })
    }

}
