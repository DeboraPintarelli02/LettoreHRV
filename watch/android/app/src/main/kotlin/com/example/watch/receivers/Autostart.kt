package com.example.watch.receivers

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.watch.MainActivity
import com.example.watch.R
import com.example.watch.services.SensorsService

class Autostart : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val sharedPreferences = context.getSharedPreferences("servizio", Context.MODE_PRIVATE)
        val servizio = sharedPreferences.getBoolean("status", false)

        if(servizio){
            val startServiceIntent = Intent(context, SensorsService::class.java)
            context.startService(startServiceIntent)
        }

    }
}