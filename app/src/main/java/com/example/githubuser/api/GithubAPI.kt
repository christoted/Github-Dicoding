package com.example.githubuser.api

import com.example.githubuser.model.GithubUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubAPI {

    @GET("/search/users?q=repos:%3E9+followers&page=1&per_page=20")
    suspend fun getSomeUser() : Response<GithubUserResponse>

    @GET("/search/users")
    suspend fun searchUser(
        @Query("q")
        loginName : String
    ) : Response<GithubUserResponse>

}