package com.movie.trvn.core.data

import com.movie.trvn.core.data.source.local.LocalDataSource
import com.movie.trvn.core.data.source.remote.RemoteDataSource
import com.movie.trvn.core.domain.model.Movie
import com.movie.trvn.core.domain.repository.IMoviesRepository
import com.movie.trvn.core.utils.DataMapper.toDomain
import com.movie.trvn.core.utils.DataMapper.toEntity

class MoviesRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,

) : IMoviesRepository{

    companion object {
        @Volatile
        private var instance: MoviesRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
        ): MoviesRepository =
            instance ?: synchronized(this) {
                instance ?: MoviesRepository(remoteData, localData)
            }
    }
    override suspend fun getAllMovies(): List<Movie>  {
        return localDataSource.getAllMovies().map { it.toDomain() }
    }

    override suspend fun insertAllMovies(movieList: List<Movie>) {
    return localDataSource.insertAllMovies(movieList.map { it.toEntity() })
    }


    override fun deleteAll() {
        return localDataSource.deleteAll()
    }

    override suspend fun fetchMovies(): List<Movie> {
        return remoteDataSource.fetchMovies().map { it.toDomain() }
    }

}