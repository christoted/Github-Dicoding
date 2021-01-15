package com.example.consumerapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey


data class GithubUserItem (
    @PrimaryKey
    val id: Int = 0,
    val login: String?=null,
    val avatar_url: String?=null,
    val type: String?=null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<GithubUserItem> {
        override fun createFromParcel(parcel: Parcel): GithubUserItem {
            return GithubUserItem(parcel)
        }

        override fun newArray(size: Int): Array<GithubUserItem?> {
            return arrayOfNulls(size)
        }
    }
}