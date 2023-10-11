package com.movie.trvn.core.domain.usecase


import com.movie.trvn.core.domain.model.Movie
import com.movie.trvn.core.domain.repository.IMoviesRepository

class MoviesInteractor(private val moviesRepository: IMoviesRepository): MoviesUseCase{
    override suspend fun getAllMovies() = moviesRepository.getAllMovies()

    override suspend fun insertAllMovies(movieList: List<Movie>) = moviesRepository.insertAllMovies(movieList)

    override fun deleteAll() = moviesRepository.deleteAll()

    override suspend fun fetchMovies() = moviesRepository.fetchMovies()


}