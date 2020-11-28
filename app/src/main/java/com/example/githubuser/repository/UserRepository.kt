package com.example.githubuser.repository

import com.example.githubuser.api.RetrofitInstance

class UserRepository() {

    //Get Remote Data
    suspend fun getSomeUser() = RetrofitInstance.api.getSomeUser()

    //Search User
    suspend fun searchUser(loginName: String) = RetrofitInstance.api.searchUser(loginName)
}