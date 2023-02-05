package com.example.fithpath.models.response

//Estrutura da resposta JSON do Login
data class LoginResponse(
    val success: Boolean,
    val data: Data,
    val message: String
)