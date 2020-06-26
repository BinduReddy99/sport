package com.binduinfo.sports.util.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SportRequest(
    val address: String,
    val area: String,
    val city: String,
    val country: String,
    val dateTime: String,
    val latitude: String,
    val longitude: String,
    val number: Int,
    val pincode: String,
    val sport: String,
    val state: String
) : Parcelable