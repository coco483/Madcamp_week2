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

    @POST("users/")
    fun createUser(@Body user: User): Call<User>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: String): Call<User>

    @PUT("users/{id}/")
    fun updateData(
        @Path("id") id: String,
        @Body user: User
    ): Call<ResponseBody>
    // 업데이트 하기 위해서 Body를 모두 업데이트 해야 한다.
}

// server가 client한테 보내는 것