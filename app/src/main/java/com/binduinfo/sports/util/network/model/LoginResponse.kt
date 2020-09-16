package com.binduinfo.sports.util.network.model

data class LoginResponse(
    val message: String = "",
    val success: Int,
    val token: String?,
    val isLocationAvailable: Boolean,
    val isInterestedSport: Boolean
)