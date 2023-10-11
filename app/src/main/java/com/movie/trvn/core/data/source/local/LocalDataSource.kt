package com.movie.trvn.core.data.source.local

import com.movie.trvn.core.data.source.local.dao.MoviesDao
import com.movie.trvn.core.data.source.local.entity.MoviesEntity

class LocalDataSource private constructor(private val moviesDao: MoviesDao) {

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(moviesDao: MoviesDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(moviesDao)
            }
    }

    suspend fun getAllMovies(): List<MoviesEntity> = moviesDao.getAllMovies()

    suspend fun insertAllMovies(movieList: List<MoviesEntity>) = moviesDao.insertAllMovies(movieList)

    fun deleteAll() = moviesDao.deleteAll()

}