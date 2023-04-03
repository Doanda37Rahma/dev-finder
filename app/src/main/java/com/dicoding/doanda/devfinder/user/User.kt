package com.dicoding.doanda.devfinder.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val avatar: String,
    val name: String,
) : Parcelable
