package com.binduinfo.sports.ui.bottomSheet.sportrequest

interface SportRequestListener {
    fun cancel()
    fun submit()
    fun sportLocationFetch()
    fun selectSport()
    fun selectDate()
    fun selectEventTime()
}