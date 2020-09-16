package com.binduinfo.sports.data.model.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressRequest(
    val address: String,
    val area: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val pincode: String,
    val state: String
) : Parcelable