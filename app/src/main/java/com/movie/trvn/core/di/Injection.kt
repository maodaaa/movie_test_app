package com.movie.trvn.core.di

import android.content.Context
import com.movie.trvn.core.data.MoviesRepository
import com.movie.trvn.core.data.source.local.LocalDataSource
import com.movie.trvn.core.data.source.local.database.MoviesDatabase
import com.movie.trvn.core.data.source.remote.RemoteDataSource
import com.movie.trvn.core.data.source.remote.network.ApiConfig
import com.movie.trvn.core.domain.repository.IMoviesRepository
import com.movie.trvn.core.domain.usecase.MoviesInteractor
import com.movie.trvn.core.domain.usecase.MoviesUseCase

object Injection {
    private fun provideRepository(context: Context): IMoviesRepository {
        val database = MoviesDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        val localDataSource = LocalDataSource.getInstance(database.moviesDao())

        return MoviesRepository.getInstance(remoteDataSource, localDataSource)
    }

    fun provideMoviesUseCase(context: Context): MoviesUseCase {
        val repository = provideRepository(context)
        return MoviesInteractor(repository)
    }
}