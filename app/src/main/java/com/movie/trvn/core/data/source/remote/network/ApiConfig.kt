package com.movie.trvn.core.data.source.remote.network

import com.movie.trvn.BuildConfig
import com.movie.trvn.core.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return builder.build()
    }


    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

//fun getApiService(): ApiService {
//    val loggingInterceptor = if(BuildConfig.DEBUG){
//        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//    } else {
//        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
//    }
//    val client = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
//        .build()
//    val retrofit = Retrofit.Builder()
//        .baseUrl(Constant.apiUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
//        .build()
//    return retrofit.create(ApiService::class.java)
//}