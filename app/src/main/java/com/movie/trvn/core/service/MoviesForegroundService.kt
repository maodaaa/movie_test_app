package com.movie.trvn.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.movie.trvn.MainActivity
import com.movie.trvn.R
import com.movie.trvn.core.di.Injection
import com.movie.trvn.core.utils.NetworkUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesForegroundService : Service() {
    private val moviesUseCase = Injection.provideMoviesUseCase(this)
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private val fetchIntervalMillis = 60_000L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "ForegroundService dijalankan...")
        fetchDataPeriodically()
        return START_STICKY
    }

    private fun fetchDataPeriodically() {
        serviceScope.launch {
            while (true) {
                Log.d(TAG, "ForegroundService fetchDataPeriodically dijalankan...")
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
                    Log.d(TAG, "ForegroundService fetchMovieData dijalankan...")
                    moviesUseCase.deleteAll()
                    moviesUseCase.insertAllMovies(movieData.take(10))

                    val notification = buildNotification()
                    startForeground(NOTIFICATION_ID, notification)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, e.message.toString())
        }
    }

    private fun buildNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )


        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Movie")
            .setContentText("Daftar Movie telah diupdate.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Automatically dismiss the notification when clicked

        // Set the notification to automatically disappear after 10 seconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12 and higher
            notificationBuilder.setTimeoutAfter(10000) // 10 seconds in milliseconds
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_NAME
            notificationBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = notificationBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)

      return notification
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_1"
        private const val CHANNEL_NAME = "movies channel"
        internal val TAG = MoviesForegroundService::class.java.simpleName
    }
}