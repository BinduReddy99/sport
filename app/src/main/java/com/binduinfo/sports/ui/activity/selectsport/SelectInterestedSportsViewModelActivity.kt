package com.binduinfo.sports.ui.activity.selectsport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.repositores.SportsRepository
import com.binduinfo.sports.util.network.model.Sport
import com.example.mvvmsample.util.Coroutines
import com.example.mvvmsample.util.lazyDeferred
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import java.lang.Exception

class SelectInterestedSportsViewModelActivity(val repository: SportsRepository) : ViewModel() {
    var recyleListFetchListener: RecyleListFetchListener? = null

    val mutableLiveData: MutableLiveData<String> = MutableLiveData<String>().apply {

    }

    val sports by lazyDeferred {
        repository.getSports()
    }

    val reqSportSelectList by lazyDeferred {
        repository.getRequestSportsList()
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

     fun updateItem(_id: String, isSelected: Boolean) {
        Coroutines.io {
            repository.updateItem(_id, isSelected)
        }
    }

    fun updateItem(previousId: String, currentId: String) {
        Coroutines.io {
            repository.updateItem(previousId, currentId)
        }
    }

    suspend fun sendSelectedSportList() {
        try {
            repository.isSportsSelected().let {
                if (it)
                    recyleListFetchListener?.sportSelectedUpdate()
            }
        } catch (e: CancellationException) {
            recyleListFetchListener?.throwable(e)
        } catch (e: Exception) {
            recyleListFetchListener?.throwable(e)
        }

    }


}
