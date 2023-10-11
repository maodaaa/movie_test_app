package com.movie.trvn

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.movie.trvn.core.domain.model.Movie
import com.movie.trvn.core.service.MoviesBackgroundService
import com.movie.trvn.core.service.MoviesBroadcast
import com.movie.trvn.core.service.MoviesForegroundService
import com.movie.trvn.core.ui.MovieAdapter
import com.movie.trvn.core.ui.ViewModelFactory
import com.movie.trvn.core.utils.UiState
import com.movie.trvn.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val moviesAdapter = MovieAdapter()


    private val movieBroadcastReceiver = MoviesBroadcast { intent ->
        when (intent.action) {
            "background_service_movie" -> {
                observeViewModel()
                showSnackbar("Penyimpanan Lokal Sudah Diperbaharui")
            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestNotificationPermission()
        setupService()
        setupUI()
        observeViewModel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackbar("Permission Granted")
            } else {
                requestNotificationPermission()
            }
        }
    }



    private fun setupService() {
        val backgroundServiceIntent = Intent(this, MoviesBackgroundService::class.java)
        startService(backgroundServiceIntent)

        val filter = IntentFilter("background_service_movie")
        LocalBroadcastManager.getInstance(this).registerReceiver(movieBroadcastReceiver, filter)

        val foregroundServiceIntent = Intent(this, MoviesForegroundService::class.java)

        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(foregroundServiceIntent)
        } else {
            startService(foregroundServiceIntent)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )
        }
    }



    private fun setupUI() {
        binding.movieRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = moviesAdapter
        }
    }


    private fun observeViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        mainViewModel.getAllMovies()
        mainViewModel.moviesLiveData.observe(this) { movie ->
            binding.progressBar.visibility = when (movie) {
                is UiState.Loading -> View.VISIBLE
                is UiState.Success -> if (movie.data.isEmpty()) View.GONE.also { showEmptyData() } else View.GONE.also { showMovieData(movie.data) }
                is UiState.Failure -> View.GONE.also { showSnackbar(movie.message) }
            }
        }
    }

    private fun showEmptyData() {
        binding.movieRv.visibility = View.GONE
        binding.emptyMovieTv.visibility = View.VISIBLE
    }

    private fun showMovieData(data: List<Movie>) {
        binding.movieRv.visibility = View.VISIBLE
        binding.emptyMovieTv.visibility = View.GONE
        moviesAdapter.setListMovies(data)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    private fun stopBackgroundService() {
        val backgroundServiceIntent = Intent(this, MoviesBackgroundService::class.java)
        stopService(backgroundServiceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(movieBroadcastReceiver)
        stopBackgroundService()
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1
    }
}

