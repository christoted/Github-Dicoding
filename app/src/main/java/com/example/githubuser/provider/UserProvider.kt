package com.example.githubuser.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.example.githubuser.dbbasic.DatabaseContract.AUTHORITY
import com.example.githubuser.dbbasic.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.dbbasic.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuser.dbbasic.UserHelper

class UserProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private lateinit var userHelper: UserHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(
                AUTHORITY,
                TABLE_NAME,
                USER
            )

            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }

    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USER_ID -> userHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added : Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insertForContentProvider(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}