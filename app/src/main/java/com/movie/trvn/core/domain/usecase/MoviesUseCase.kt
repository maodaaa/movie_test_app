package com.movie.trvn.core.domain.usecase

import com.movie.trvn.core.domain.model.Movie

interface MoviesUseCase {
    suspend fun getAllMovies(): List<Movie>

    suspend fun insertAllMovies(movieList: List<Movie>)

    fun deleteAll()

    suspend fun fetchMovies() : List<Movie>
}