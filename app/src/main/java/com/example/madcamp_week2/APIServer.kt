package com.example.madcamp_week2

import User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServer {
    @GET("todoItems/")
    fun getItems(): Call<List<TodoItem>>

    @POST("users/")
    fun createUser(@Body user: User): Call<User>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: String): Call<User>

    @POST("users/{id}/favorites")
    fun updateFavoriteList(
        @Path("id") id: String,
        @Body favorites: String
    ): Call<ResponseBody>

    @POST("users/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body user: User
    ): Call<User>
}

// server가 client한테 보내는 것