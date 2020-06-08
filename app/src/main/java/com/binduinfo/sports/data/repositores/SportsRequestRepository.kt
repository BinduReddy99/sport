package com.binduinfo.sports.data.repositores

import com.miziontrix.kmo.data.network.api.mvvm.MyApi
import com.miziontrix.kmo.data.network.api.mvvm.SafeAPIRequest

class SportsRequestRepository(private val api: MyApi): SafeAPIRequest()  {

}