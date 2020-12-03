package com.example.githubuser.model

import android.os.Parcel
import android.os.Parcelable

data class GithubUserItem(
    val avatar_url: String?,
    val events_url: String?,
    val followers_url: String?,
    val following_url: String?,
    val gists_url: String?,
    val gravatar_id: String?,
    val html_url: String?,
    val id: Int,
    val login: String?,
    val node_id: String?,
    val organizations_url: String?,
    val received_events_url: String?,
    val repos_url: String?,
    val score: Double,
    val site_admin: Boolean,
    val starred_url: String?,
    val subscriptions_url: String?,
    val type: String?,
    val url: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatar_url)
        parcel.writeString(events_url)
        parcel.writeString(followers_url)
        parcel.writeString(following_url)
        parcel.writeString(gists_url)
        parcel.writeString(gravatar_id)
        parcel.writeString(html_url)
        parcel.writeInt(id)
        parcel.writeString(login)
        parcel.writeString(node_id)
        parcel.writeString(organizations_url)
        parcel.writeString(received_events_url)
        parcel.writeString(repos_url)
        parcel.writeDouble(score)
        parcel.writeByte(if (site_admin) 1 else 0)
        parcel.writeString(starred_url)
        parcel.writeString(subscriptions_url)
        parcel.writeString(type)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
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