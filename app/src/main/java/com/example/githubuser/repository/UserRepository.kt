package com.example.githubuser.repository

import com.example.githubuser.api.RetrofitInstance

class UserRepository() {

    //Get Remote Data
    suspend fun getSomeUser() = RetrofitInstance.api.getSomeUser()

    //Search User
    suspend fun searchUser(q: String) = RetrofitInstance.api.searchUser(q)

    //Get all Followers user
    suspend fun getFollower(login: String) = RetrofitInstance.api.getFollower(login)

    //Get all Followers user
    suspend fun getFollowing(login: String) = RetrofitInstance.api.getFollowing(login)

}