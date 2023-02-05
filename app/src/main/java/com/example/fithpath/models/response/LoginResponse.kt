package com.example.fithpath.models.response

data class LoginResponse(
    val success: Boolean,
    val data: Data,
    val message: String
)