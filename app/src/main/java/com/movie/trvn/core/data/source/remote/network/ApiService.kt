package com.movie.trvn.core.data.source.remote.network

import com.movie.trvn.core.data.source.remote.model.MoviesResponse
import com.movie.trvn.core.utils.Constant
import retrofit2.http.GET

interface ApiService {
    @GET("discover/movie?api_key=${Constant.apiKey}")
    suspend fun fetchMovies(): MoviesResponse
}