package com.example.retrofitapp.remote

import com.example.retrofitapp.remote_models.UserActionRequestWrapper
import com.example.retrofitapp.remote_models.UsersWrapper
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
//implementation ("com.squareup.okhttp3:okhttp:4.9.3")
//implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
//implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//implementation ("com.squareup.retrofit2:converter-gson:2.5.0")
interface UserApi {
    @GET("/users")
    suspend fun getAll(): UsersWrapper

    @GET("/users/{userId}")
    suspend fun getById(@Path("userId") id: Int): UsersWrapper

    @DELETE("/users/{userId}")
    suspend fun deleteById(@Path("userId") id: Int): Map<String, String>

    @POST("/users")
    suspend fun add(@Body body: UserActionRequestWrapper): UsersWrapper

    @PUT("/users/{userId}")
    suspend fun update(@Path("userId") id: Int, @Body body: UserActionRequestWrapper): UsersWrapper
}