package com.binduinfo.sports.ui.activity.selectsport

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.repositores.SportsRepository
import com.binduinfo.sports.util.network.model.Sport
import com.binduinfo.sports.util.network.retrofit.NetworkInterFace
import com.example.mvvmsample.util.Coroutines
import com.example.mvvmsample.util.lazyDeferred
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import java.lang.Appendable
import java.lang.Exception
import java.lang.NullPointerException

class SelectInterestedSportsViewModel(val repository: SportsRepository) : ViewModel() {
    var recyleListFetchListener: RecyleListFetchListener? = null

    val mutableLiveData: MutableLiveData<String> = MutableLiveData<String>().apply {

    }

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

    suspend fun sendSelectedSportList(){
        try {
            repository.sendSelectedSportList().let {
                recyleListFetchListener?.sportSelectedUpdate(it)
            }
        }catch (e: CancellationException){
            recyleListFetchListener?.throwable(e)
        }catch (e: Exception){
            recyleListFetchListener?.throwable(e)
        }

    }


}
