package com.binduinfo.sports.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateProfile(
    val email: String,
    val gender: String,
    val mobileNumber: String,
    val name: String
): Parcelable