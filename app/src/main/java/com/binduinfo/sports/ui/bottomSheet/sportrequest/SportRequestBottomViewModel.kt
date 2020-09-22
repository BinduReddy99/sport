package com.binduinfo.sports.ui.bottomSheet.sportrequest

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.repositores.SportsRequestRepository
import com.binduinfo.sports.util.network.model.SportRequest
import com.binduinfo.sports.util.network.model.SportRequestEventResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async


class SportRequestBottomViewModel(private val repository: SportsRequestRepository) : ViewModel() {
    val serverRequest: MutableLiveData<Boolean> = MutableLiveData()
    val address: MutableLiveData<SportRequest> = MutableLiveData()
    val sport: MutableLiveData<SportRequest> = MutableLiveData()
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

    suspend fun onClickConfirmRequest(sportRequest: SportRequest): Deferred<SportRequestEventResponse> {
//         sportRequestListener?.confirmRequest()
        return CoroutineScope(IO).async {
            repository.sportRequestEvent(sportRequest)
        }
    }

//    suspend fun selectOneSportRequest(sportRequest: SportRequest): Deferred<SportRequestEventResponse> {
//     return CoroutineScope(IO).async{
//         repository.sportRequestEvent(sportRequest)
//     }
//
//    }


}
