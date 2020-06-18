package com.binduinfo.sports.util.network.model

data class SportRequestEventResponse(
    val sports: List<SportRequestEvent>,
    val success: Int
)