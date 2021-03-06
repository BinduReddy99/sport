package com.binduinfo.sports.ui.fragment.selectinterestedsports


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.repositores.SportsRepository
import kotlinx.coroutines.CancellationException

class SelectInterestedSportsViewModel(val repository: SportsRepository) : ViewModel() {
    var selectSportInterface: SelectSportsInterface? = null

    val mutableLiveData: MutableLiveData<String> = MutableLiveData<String>().apply {

    }

//    val sports by lazyDeferred{
//        repository.getSports()
//    }
//
//    fun sportType(sportType: String): Lazy<Deferred<LiveData<List<Sport>>>> {
//        return lazyDeferred {
//            repository.selectBySportType(sportType)
//        }
//    }
//
//    fun sportType(): Lazy<Deferred<LiveData<List<Sport>>>> {
//        return lazyDeferred {
//            repository.allSport()
//        }
//    }
//
//    fun updateItem(_id: String, isSelected: Boolean){
//        Coroutines.io {
//            repository.updateItem(_id, isSelected)
//        }
//    }

    suspend fun sendSelectedSportList() {
        try {
            repository.sendSelectedSportList().let {
                selectSportInterface?.sportSelectedUpdate(it)
            }
        } catch (e: CancellationException) {
            selectSportInterface?.throwable(e)
        } catch (e: Exception) {
            selectSportInterface?.throwable(e)
        }

    }


}
