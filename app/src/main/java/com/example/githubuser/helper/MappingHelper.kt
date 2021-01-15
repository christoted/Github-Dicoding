package com.example.githubuser.helper

import android.database.Cursor
import android.provider.ContactsContract
import com.example.githubuser.dbbasic.DatabaseContract
import com.example.githubuser.model.GithubUserItem

object MappingHelper {
    fun convertCursorToArrayList(userCursor: Cursor?) : ArrayList<GithubUserItem>{
        val listUser = ArrayList<GithubUserItem>()

        userCursor?.apply {
            while(moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
                val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.TYPE))
                listUser.add(GithubUserItem(id, login, avatar_url, type))
            }
        }

        return listUser
    }

    fun mapCursorToObject(userCursor: Cursor?): GithubUserItem {
        var githubUserItem = GithubUserItem()
        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
            val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
            val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.TYPE))
            githubUserItem = GithubUserItem(id, login, avatar_url, type)
        }

        return githubUserItem
    }
}