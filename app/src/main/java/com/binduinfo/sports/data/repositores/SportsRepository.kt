package com.binduinfo.sports.data.repositores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.util.network.model.Sport
import com.example.mvvmsample.util.Coroutines
import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.SafeAPIRequest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class SportsRepository(private val api: MyApi, private val db: AppDataBase): SafeAPIRequest() {
    private val sports = MutableLiveData<List<Sport>>()
    init {
        sports.observeForever {
            saveSports(it)
        }
    }
    suspend fun getSports(): LiveData<List<Sport>> {
        return withContext(IO){
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
}