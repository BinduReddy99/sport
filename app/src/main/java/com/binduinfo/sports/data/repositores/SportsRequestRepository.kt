package com.binduinfo.sports.data.repositores

import com.binduinfo.sports.data.db.entity.AppDataBase
import com.binduinfo.sports.data.network.mvvm.MyApi
import com.binduinfo.sports.data.network.mvvm.SafeAPIRequest


class SportsRequestRepository(private val api: MyApi , private val db: AppDataBase): SafeAPIRequest()  {



}