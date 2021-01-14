package com.example.githubuser.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuser.database.UserDAO
import com.example.githubuser.database.UserDatabase
import com.example.githubuser.model.GithubUserItem

class UserProvider : ContentProvider() {

    companion object {
        private const val USER = 1

        const val AUTHORITY = "com.example.githubuser"
        val URI_MENU = Uri.parse("content://$AUTHORITY/users")

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init{
            sUriMatcher.addURI(
                AUTHORITY,
                "users",
                USER
            )
        }

    }

    override fun onCreate(): Boolean {
        return true
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO()
    }


    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val code = sUriMatcher.match(uri)
        return if (code == USER) {
            val context = context ?: return null
            val userDAO: UserDAO = UserDatabase.invoke(context).getUserDAO()

            val cursor : Cursor
            cursor = if ( code == USER) {
                userDAO.getAllSavedUsers() as Cursor
            } else {
                return null
            }
            cursor.setNotificationUri(context.contentResolver, uri)
            cursor

        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}