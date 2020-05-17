package com.binduinfo.sports.ui.fragment.selectinterestedsports

import com.binduinfo.sports.util.network.model.Sport

interface RecyleListFetchListener {
    fun sports(sportsList: List<Sport>)
    fun throwable(throwable: Throwable)
    fun filter(sportsList: List<Sport>)
}