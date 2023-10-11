package com.movie.trvn.splashscreen

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.movie.trvn.MainActivity
import com.movie.trvn.R
import com.movie.trvn.core.utils.NetworkUtil

class SplassScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splass_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            checkNetworkConnection()
        }, 2000)
    }

    private fun checkNetworkConnection() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            showNetworkErrorDialog()
        } else {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showNetworkErrorDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Koneksi Error")
            .setMessage("Periksa jaringan internet anda dan coba lagi.")
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }
}
