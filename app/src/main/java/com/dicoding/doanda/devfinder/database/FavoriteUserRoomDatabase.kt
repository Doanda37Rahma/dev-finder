package com.dicoding.doanda.devfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUser::class], version = 2)
abstract class FavoriteUserRoomDatabase : RoomDatabase() {

    abstract fun userDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteUserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteUserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteUserRoomDatabase::class.java,
                        "user_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as FavoriteUserRoomDatabase
        }
    }
}