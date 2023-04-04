package com.dicoding.doanda.devfinder.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    var avatar: String?,
    var username: String?,
    var name: String?,
    var followerCount: String?,
    var followingCount: String?,
) : Parcelable
