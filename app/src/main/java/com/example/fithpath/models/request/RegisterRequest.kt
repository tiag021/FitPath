package com.example.fithpath.models.request

//Estrutura do form-data a enviar para realizar o Registo
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirm_password: String
)