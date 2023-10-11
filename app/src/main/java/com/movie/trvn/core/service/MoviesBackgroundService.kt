package com.movie.trvn.core.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.movie.trvn.core.di.Injection
import com.movie.trvn.core.utils.NetworkUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesBackgroundService : Service() {
    private val moviesUseCase = Injection.provideMoviesUseCase(this)
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private val fetchIntervalMillis = 60_000L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service dijalankan...")
        fetchDataPeriodically()
        return START_STICKY
    }

    private fun fetchDataPeriodically() {
        serviceScope.launch {
            while (true) {
                Log.d(TAG, "Service fetchDataPeriodically dijalankan...")
                fetchMovieData()
                delay(fetchIntervalMillis)
            }
        }
    }


    private suspend fun fetchMovieData() {
        try {
           if ( NetworkUtil.isNetworkAvailable(this)){
               val movieData = moviesUseCase.fetchMovies()
               if (movieData.isNotEmpty()) {
                   Log.d(TAG, "Service fetchMovieData dijalankan...")
                   moviesUseCase.deleteAll()
                   moviesUseCase.insertAllMovies(movieData.take(10))

                   val intent = Intent("background_service_movie")
                   LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
               }
           }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, e.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    companion object {
        internal val TAG = MoviesBackgroundService::class.java.simpleName
    }
}