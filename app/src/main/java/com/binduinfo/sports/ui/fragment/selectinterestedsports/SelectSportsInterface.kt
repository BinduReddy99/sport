package com.binduinfo.sports.ui.fragment.selectinterestedsports

import com.binduinfo.sports.data.model.BasicModel

interface SelectSportsInterface {
   // abstract var selectSport: String?

    fun throwable(throwable: Throwable)
    fun sportSelectedUpdate(baseModel: BasicModel)
}