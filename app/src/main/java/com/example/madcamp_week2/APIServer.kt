package com.example.madcamp_week2

import User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIServer {
    @GET("todoItems/")
    fun getItems(): Call<List<TodoItem>>

    @POST("users/")
    fun createUser(@Body user: User): Call<User>
}

// server가 client한테 보내는 것