package com.binduinfo.sports.data.model

data class Address(
    val address: String,
    val area: String,
    val city: String,
    val location: List<Double>,
    val pincode: String,
    val state: String
)