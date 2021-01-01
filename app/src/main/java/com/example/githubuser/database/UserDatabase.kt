package com.example.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.model.GithubUserItem

@Database(
    entities = [GithubUserItem::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDAO(): UserDAO

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user.db"
        ).build()
    }

}