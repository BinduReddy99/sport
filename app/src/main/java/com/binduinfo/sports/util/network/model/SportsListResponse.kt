package com.binduinfo.sports.util.network.model

data class SportsListResponse(
    val sports: List<Sport>,//  Generic class
    val success: Int
)