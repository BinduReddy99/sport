package com.binduinfo.sports.ui.activity.selectsport

import com.binduinfo.sports.util.network.model.Sport

interface RecyleListFetchListener {
    fun sports(sportsList: List<Sport>)
    fun throwable(throwable: Throwable)
    fun sportSelectedUpdate()
    fun filter(sportsList: List<Sport>)
}