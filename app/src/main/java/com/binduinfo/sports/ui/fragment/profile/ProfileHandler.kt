package com.binduinfo.sports.ui.fragment.profile

import com.binduinfo.sports.data.model.About
import com.binduinfo.sports.data.model.UpdateProfile

interface ProfileHandler {
    fun profilePic()
    fun profilePicUpdate()
    fun logout()
    fun selectSport()
    fun updateProfileInfo(updateProfileInfo: UpdateProfile)
    fun updateAboutInProfile(updateAboutMe:About)
    fun profileLocationEdit()
}