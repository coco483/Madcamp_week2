package com.example.madcamp_week2

import retrofit2.Call
import retrofit2.http.GET

interface APIServer {
    @GET("todoItems/")
    fun getItems(): Call<List<TodoItem>>
}