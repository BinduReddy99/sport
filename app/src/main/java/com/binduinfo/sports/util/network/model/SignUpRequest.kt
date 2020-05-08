package com.binduinfo.sports.util.network.model

data class SignUpRequest(
    val confirmPassword: String,
    val email: String,
    val gender: String,
    val mobileNumber: String,
    val name: String,
    val otp: String,
    val password: String
)