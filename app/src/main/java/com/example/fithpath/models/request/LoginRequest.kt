package com.example.fithpath.models.request

//Estrutura do form-data a enviar para realizar o Login
data class LoginRequest(
    val email: String,
    val password: String
)