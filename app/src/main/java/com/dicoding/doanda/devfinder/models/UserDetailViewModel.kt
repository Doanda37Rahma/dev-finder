package com.dicoding.doanda.devfinder.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.doanda.devfinder.activities.UserDetailActivity
import com.dicoding.doanda.devfinder.database.FavoriteUser
import com.dicoding.doanda.devfinder.network.ApiConfig
import com.dicoding.doanda.devfinder.network.GithubUserResponse
import com.dicoding.doanda.devfinder.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application, private val userName: String) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteUserRepository.getFavoriteUserByUsername(username)

    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    init {
        findUserDetail()
    }

    private fun findUserDetail() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(userName)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {

                        val user = UserDetail(
                            avatar = responseBody.avatarUrl,
                            username = responseBody.login,
                            name = responseBody.name,
                            followerCount = responseBody.followers.toString(),
                            followingCount = responseBody.following.toString(),
                        )
                        _userDetail.value = user
                    }
                } else {
                    Log.e(UserDetailActivity.TAG, "onResponseIsUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(UserDetailActivity.TAG, "onFailuer: ${t.message}")
            }

        })
    }

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }
}