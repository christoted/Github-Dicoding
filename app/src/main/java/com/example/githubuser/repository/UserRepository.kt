package com.example.githubuser.repository

import com.example.githubuser.api.RetrofitInstance
import com.example.githubuser.database.UserDatabase
import com.example.githubuser.model.GithubUserItem

class UserRepository(
    val db : UserDatabase
) {

    //Get Remote Data
    suspend fun getSomeUser() = RetrofitInstance.api.getSomeUser()

    //Search User
    suspend fun searchUser(q: String) = RetrofitInstance.api.searchUser(q)

    //Get all Followers user
    suspend fun getFollower(login: String) = RetrofitInstance.api.getFollower(login)

    //Get all Followers user
    suspend fun getFollowing(login: String) = RetrofitInstance.api.getFollowing(login)

    //Get all Repository
    suspend fun getRepository(login: String) = RetrofitInstance.api.getRepo(login)

    //Add to Favourite
    suspend fun upsert(githubUserItem: GithubUserItem) = db.getUserDAO().upsert(githubUserItem)

    //Get all Save Data
    fun getAllFavouriteUsers() = db.getUserDAO().getAllSavedUsers()

    //Delete favourite
    suspend fun delete(githubUserItem: GithubUserItem) = db.getUserDAO().deleteSavedUser(githubUserItem)

}