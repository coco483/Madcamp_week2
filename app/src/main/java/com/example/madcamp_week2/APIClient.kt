package com.example.madcamp_week2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SERVER_IP = "143.248.191.144"
object ApiClient {
    private const val BASE_URL = "http://$SERVER_IP:8000/api/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: APIServer by lazy {
        retrofit.create(APIServer::class.java)
    }
}