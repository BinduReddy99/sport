package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.db.AppDataBase
import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest
import com.binduinfo.sports.util.network.model.SportRequest
import com.binduinfo.sports.util.network.model.SportRequestEventResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber


class SportsRequestRepository(private val api: MyApi, private val db: AppDataBase) :
    SafeAPIRequest() {
    suspend fun sportsRequest() {
        apiRequest {
            api.getRequestSportsList()
        }
    }

    suspend fun sportRequestEvent(sportRequest: SportRequest): SportRequestEventResponse {
        return withContext(IO) {
            Timber.d("=====serversent=======$sportRequest")
//            suspend fun selectSportRequest() {
                apiRequest {
                    api.requestSportEvent(sportRequest)
                }
            }
        }

    }
//suspend fun insertResponse(contactDetails: ContactDetails): Long{
//    return withContext(IO){
//        db.getContactDetails().saveContacts(contactDetails)
//    }
//}