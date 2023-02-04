package com.example.fithpath.models.response

data class RegisterResponse(
    val success: Boolean,
    val token: String,
    val name: String,
    val message: String
)