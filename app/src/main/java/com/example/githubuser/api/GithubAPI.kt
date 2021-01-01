package com.example.githubuser.api

import com.example.githubuser.model.GithubFollowerResponse
import com.example.githubuser.model.GithubRepoResponse
import com.example.githubuser.model.GithubUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAPI {

    @GET("/search/users?q=repos:%3E9+followers&page=1&per_page=20")
    suspend fun getSomeUser() : Response<GithubUserResponse>

    @GET("/search/users")
    suspend fun searchUser(
        @Query("q")
        loginName : String
    ) : Response<GithubUserResponse>

    @GET("/users/{login}/followers")
    suspend fun getFollower(
        @Path("login")
        login : String
    ) : Response<GithubFollowerResponse>

    @GET("/users/{login}/following")
    suspend fun getFollowing(
        @Path("login")
        login : String
    ) : Response<GithubFollowerResponse>

    @GET("users/{login}/repos")
    suspend fun getRepo(
        @Path("login")
        login: String
    ) : Response<GithubRepoResponse>

}