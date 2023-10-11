package com.movie.trvn.core.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.movie.trvn.core.data.source.local.dao.MoviesDao
import com.movie.trvn.core.data.source.local.entity.MoviesEntity





@Database(entities = [MoviesEntity::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): MoviesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDatabase::class.java, "movies.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}