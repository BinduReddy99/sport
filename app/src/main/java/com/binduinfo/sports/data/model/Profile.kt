package com.binduinfo.sports.data.model

data class Profile(
    val address: Address,
    val email: String,
    val gender: String,
    val mobileNumber: String,
    val name: String,
    val profilePic: String,
    val sports: List<Sport>,
    val about: String = ""
)