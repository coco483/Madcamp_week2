package com.example.madcamp_week2

import User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIServer {
    @GET("todoItems/")
    fun getItems(): Call<List<TodoItem>>

    @POST("/")
    fun createUser(@Body user: User): Call<User>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: String): Call<User>

    @PUT("users/{id}/")
    fun updateFavoriteList(
        @Path("id") id: String,
        @Body favorites: String
    ): Call<ResponseBody>
//
//    @POST("users/")
//    fun updateUser(
//        @Body user: User
//    ): Call<User>

    @PUT("users/{id}/")
    fun updateName(
        @Path("id") id: String,
        @Body user: User
    ): Call<ResponseBody>
}

// server가 client한테 보내는 것