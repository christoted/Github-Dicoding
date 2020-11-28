package com.example.githubuser.api

import com.example.githubuser.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubAPI {

    @GET("/users")
    suspend fun getSomeUser() : Response<UserResponse>

    @GET("/users")
    suspend fun searchUser(
        @Query("loginName")
        loginName : String
    ) : Response<UserResponse>

}