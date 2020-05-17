package com.binduinfo.sports.ui.fragment.selectinterestedsports

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.repositores.SportsRepository
import com.binduinfo.sports.util.network.model.Sport
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import com.example.mvvmsample.util.Coroutines
import com.example.mvvmsample.util.lazyDeferred
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import java.lang.Appendable

class SelectInterestedSportsViewModel(val repository: SportsRepository) : ViewModel() {
    var recyleListFetchListener: RecyleListFetchListener? = null

    val sports by lazyDeferred{
        repository.getSports()
    }

    fun sportType(sportType: String): Lazy<Deferred<LiveData<List<Sport>>>> {
        return lazyDeferred {
            repository.selectBySportType(sportType)
        }
    }

    fun sportType(): Lazy<Deferred<LiveData<List<Sport>>>> {
        return lazyDeferred {
            repository.allSport()
        }
    }

    fun updateItem(_id: String, isSelected: Boolean){
        Coroutines.io {
            repository.updateItem(_id, isSelected)
        }
    }


    //val sports
//    @SuppressLint("CheckResult")
//    fun serverRequest(){
//        networkInterFace.getSportsList(1, "all").observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                recyleListFetchListener?.sports(it.sports)
//
//            },{
//                recyleListFetchListener?.throwable(it)
//            })
//    }
//
//     fun filterBasedOnType(sports: List<Sport>, filterType: String){
//        var sportsList= arrayListOf<Sport>()
//         if (filterType.isNotEmpty())
//        sports.forEachIndexed { _, sport ->
//            if(sport.sportType == filterType)
//                sportsList.add(sport)
//        }
//         else
//             sportsList.addAll(sports)
//        recyleListFetchListener?.filter(sportsList)
//
//    }

}
