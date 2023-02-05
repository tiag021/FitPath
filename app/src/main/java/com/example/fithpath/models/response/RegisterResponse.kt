package com.example.fithpath.models.response

//Estrutura de Dados JSON da resposta obtida durante o registo
data class RegisterResponse(
    val success: Boolean,
    val token: String,
    val name: String,
    val message: String
)