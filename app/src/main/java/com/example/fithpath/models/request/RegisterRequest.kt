package com.example.fithpath.models.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirm_password: String
)