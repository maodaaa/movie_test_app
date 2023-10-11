package com.movie.trvn.core.data.source.remote

import android.util.Log
import com.movie.trvn.core.data.source.remote.model.MovieData
import com.movie.trvn.core.data.source.remote.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RemoteDataSource private constructor(private val apiService: ApiService) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }


    suspend fun fetchMovies(): List<MovieData> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.fetchMovies()
            return@withContext response.results
        } catch (e: Exception) {
            // Log the error
            Log.e("FetchMoviesError", "Error fetching movies: ${e.message}", e)
            throw e
        }
    }

}