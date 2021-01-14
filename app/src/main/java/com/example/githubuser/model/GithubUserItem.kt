package com.example.githubuser.model

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_user_detail.*

@Entity(
    tableName = "users"
)
@Parcelize
data class GithubUserItem(
    @PrimaryKey
    val id: Int,
    val login: String?,
    val avatar_url: String?,
    val type: String?

) : Parcelable