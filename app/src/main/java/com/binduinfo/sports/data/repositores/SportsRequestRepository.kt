package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest
import com.binduinfo.sports.util.network.model.SportRequest
import io.reactivex.annotations.SchedulerSupport.IO
import kotlinx.coroutines.withContext


class SportsRequestRepository(private val api: MyApi, private val db: AppDataBase) :
    SafeAPIRequest() {
    suspend fun sportsRequest() {
        apiRequest {
            api.getRequestSportsList()
        }
    }

    suspend fun sportRequestEvent(sportRequest: SportRequest) {
//        return withContext(IO) {
//            apiRequest {
//                api.requestSportEvent(SportRequest(sportRequest))
//            }
//        }
    }
}