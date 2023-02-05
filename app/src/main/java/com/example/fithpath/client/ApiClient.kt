package com.example.fithpath.client
/*
Networking on Android Using Retrofit 2 And Restful API
https://www.youtube.com/watch?v=tmu6ou-Ur9M
 */
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.example.fithpath.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun getRetrofit(): Retrofit{
        //Logging da conexão
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        //Construtor do Client com logging
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://projectferret.live")
            .client(client)
            .build()

        return retrofit
    }

    fun getApiService(): ApiService{
        return getRetrofit().create(ApiService::class.java)
    }
}