package com.dicoding.doanda.devfinder

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    https://api.github.com/search/users?q=doanda
    @GET("search/users")
    fun searchUsers(
        @Query("q") q: String
    ): Call<GithubSearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<GithubUserResponse>

}