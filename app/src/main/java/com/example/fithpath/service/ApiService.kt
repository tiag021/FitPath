package com.example.fithpath.service

import com.example.fithpath.models.request.LoginRequest
import com.example.fithpath.models.request.RegisterRequest
import com.example.fithpath.models.response.LoginResponse
import com.example.fithpath.models.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

//endpoints disponibilizados pela API
interface ApiService {
    @POST("/api/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}