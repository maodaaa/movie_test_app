package com.movie.trvn.core.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class MoviesBroadcast(private val updateCallback: (intent: Intent) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            when (intent.action) {
                "background_service_movie" -> {
                    updateCallback(intent)
                }
            }
        }
    }
}

//class MyReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        if (intent != null) {
//            when (intent.action) {
//                "data_updated_action" -> {
//                    updateCallback(intent)
//                }
//                "action_two" -> {
//                    // Handle action_two
//                }
//                // Add more cases for additional actions
//            }
//        }
//    }
//}
