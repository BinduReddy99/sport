package com.binduinfo.sports.ui.bottomSheet.sportrequest

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.repositores.SportsRequestRepository
import com.binduinfo.sports.util.network.model.SportRequest
import com.binduinfo.sports.util.network.model.SportRequestEventResponse
import com.example.mvvmsample.util.Coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class SportRequestBottomViewModel(private val repository: SportsRequestRepository) : ViewModel() {
    val serverRequest: MutableLiveData<Boolean> = MutableLiveData()
    val address: MutableLiveData<SportRequest> = MutableLiveData()
    val sport:MutableLiveData<SportRequest> =MutableLiveData()
    var sportRequestListener: SportRequestListener? = null

    fun locate(view: View) {
        sportRequestListener?.sportLocationFetch()
    }

    fun selectOneSport(view: View) {
        sportRequestListener?.selectSport()

    }

    fun selectTime(view: View) {
        sportRequestListener?.selectEventTime()
    }

    fun selectDate(view: View) {
        sportRequestListener?.selectDate()
    }
     fun onClickConfirmRequest(view: View){
         sportRequestListener?.confirmRequest()
     }

    suspend fun selectOneSportRequest(sportRequest: SportRequest): Deferred<SportRequestEventResponse> {
     return CoroutineScope(IO).async{
         repository.sportRequestEvent(sportRequest)
     }

    }


}
