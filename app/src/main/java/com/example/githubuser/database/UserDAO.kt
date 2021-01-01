package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.githubuser.model.GithubUserItem

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(githubUserItem: GithubUserItem)

    @Query("SELECT * FROM users")
    fun getAllSavedUsers() : LiveData<List<GithubUserItem>>

    @Delete
    suspend fun deleteSavedUser(githubUserItem: GithubUserItem)
}