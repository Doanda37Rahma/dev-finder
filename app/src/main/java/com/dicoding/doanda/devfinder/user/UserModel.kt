package com.dicoding.doanda.devfinder.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var avatar: String?,
    var username: String?,
    var name: String?,
    var followerCount: String?,
    var followingCount: String?,
) : Parcelable
