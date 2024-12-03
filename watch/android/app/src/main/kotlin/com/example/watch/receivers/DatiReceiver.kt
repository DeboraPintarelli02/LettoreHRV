package com.example.watch.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.watch.R
import com.example.watch.services.MobileService
import com.example.watch.services.SensorsService

class DatiReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val channel = NotificationChannel(
            "FOREGROUND_SERVICE",
            "PennSkanvTicChannel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "canale delle notifiche per l'app in background"

        val notificationManager  = context?.getSystemService<NotificationManager>(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)

        val notification: Notification = Notification.Builder(context, "FOREGROUND_SERVICE")
            .setContentTitle("Servizio in esecuzione")
            .setContentText("Il servizio è attivo in background")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
            .build()

        notificationManager?.notify(1, notification)

        if(!SensorsService.status){
            val intent = Intent(context, SensorsService::class.java)
            context?.startService(intent)
        }

        if(!MobileService.status){
           val intent = Intent(context, MobileService::class.java)
           context?.startService(intent)
        }
    }
}