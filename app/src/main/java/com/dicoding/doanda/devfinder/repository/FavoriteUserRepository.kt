package com.dicoding.doanda.devfinder.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.doanda.devfinder.database.FavoriteUser
import com.dicoding.doanda.devfinder.database.FavoriteUserDao
import com.dicoding.doanda.devfinder.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.userDao()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteUserDao.getFavoriteUserByUsername(username)

    fun getAllUsers(): LiveData<List<FavoriteUser>> =
        mFavoriteUserDao.getAllNotes()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }

    fun update(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.update(favoriteUser) }
    }
}