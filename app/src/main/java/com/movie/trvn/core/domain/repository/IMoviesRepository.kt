package com.movie.trvn.core.domain.repository

import com.movie.trvn.core.domain.model.Movie

interface IMoviesRepository {

    suspend fun getAllMovies(): List<Movie>

    suspend fun insertAllMovies(movieList: List<Movie>)

    fun deleteAll()

    suspend fun fetchMovies() : List<Movie>

}