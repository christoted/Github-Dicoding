package com.example.githubuser.dbbasic

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    class UserColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "pengguna"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val TYPE = "type"
        }

    }
}