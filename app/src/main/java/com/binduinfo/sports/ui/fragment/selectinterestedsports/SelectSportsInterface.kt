package com.binduinfo.sports.ui.fragment.selectinterestedsports

import com.binduinfo.sports.data.model.BasicModel

interface SelectSportsInterface {
    fun throwable(throwable: Throwable)
    fun sportSelectedUpdate(baseModel: BasicModel)
}