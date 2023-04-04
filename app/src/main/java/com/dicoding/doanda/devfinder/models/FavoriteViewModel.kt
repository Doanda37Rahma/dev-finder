package com.dicoding.doanda.devfinder.models

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.doanda.devfinder.database.FavoriteUser
import com.dicoding.doanda.devfinder.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllUsers()
}