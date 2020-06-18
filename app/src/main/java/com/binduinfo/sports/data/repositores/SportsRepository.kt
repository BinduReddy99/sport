package com.binduinfo.sports.data.repositores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.sport.RequestInterestSport
import com.binduinfo.sports.util.network.model.Sport
import com.example.mvvmsample.util.Coroutines
import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest
import com.binduinfo.sports.util.network.model.SportRequestEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber

class SportsRepository(private val api: MyApi, private val db: AppDataBase): SafeAPIRequest() {
    private val sports = MutableLiveData<List<Sport>>()
    init {
        sports.observeForever {
            saveSports(it)
        }
    }
    suspend fun getSports(): LiveData<List<Sport>> {
        return withContext(IO){
            fetchSportRequestEvent()
            fetchSports()
            db.getUserSport().getSports()
        }
    }

    private suspend fun fetchSports(){
        val response = apiRequest { api.getSportsList() }
        sports.postValue(response.sports)
    }

    private fun saveSports(sports: List<Sport>){
        Coroutines.io {
            db.getUserSport().saveAllSports(sports)
        }
    }

    suspend fun selectBySportType(type:String): LiveData<List<Sport>>{
        return withContext(IO){
            db.getUserSport().getSports(type)
        }
    }

    suspend fun allSport(): LiveData<List<Sport>>{
        return withContext(IO){
            db.getUserSport().getSports()
        }
    }
    suspend fun updateItem(_id: String, isSelected: Boolean){
        db.getUserSport().updateItem(_id, isSelected)
    }

    suspend  fun sendSelectedSportList(): BasicModel{

            val selectedItem = retrieveSelectedItem()
            if(selectedItem.isNotEmpty())
             return   apiRequest { api.updateInterestedSport(RequestInterestSport(selectedItem)) }
            else{
                throw CancellationException("Please select sports and try again")
            }
    }

    suspend fun isSportsSelected(): Boolean{
        val selectedItem = retrieveSelectedItem()
        if(selectedItem.isNotEmpty())
            return true
        else{
            throw CancellationException("Please select sports and try again")
        }
    }

    suspend fun retrieveSelectedItem():List<String>{
       return withContext(IO){
          db.getUserSport().selectSelectedSports()
        }
    }
    private suspend fun fetchSportRequestEvent() {
        val responseEvent = apiRequest { api.requestSportEvent() }
       sports.postValue(responseEvent.sports)
        Timber.d("=====repository======${responseEvent}")
    }
}



private fun <T> MutableLiveData<T>.postValue(sports: List<SportRequestEvent>) {

}
