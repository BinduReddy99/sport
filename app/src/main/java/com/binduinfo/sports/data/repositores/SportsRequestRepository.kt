package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest


class SportsRequestRepository(private val api: MyApi): SafeAPIRequest()  {

}