package com.example.watch.manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.watch.receivers.DatiReceiver
import com.example.watch.services.MobileService
import com.example.watch.services.SensorsService

class ClockManager(val context: Context) {

    fun startClock(){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DatiReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        val interval: Long = 60 * 1000
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent)

        val intentMobile = Intent(context, MobileService::class.java)
        context.startService(intentMobile)
    }

    fun checkStatus(): Boolean{
        val intent = Intent(context, DatiReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE)


        return pendingIntent != null
    }

    fun stopService(){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DatiReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        // Cancella l'allarme
        alarmManager.cancel(pendingIntent)

        PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE).cancel()

        val intentMobile: Intent = Intent(context, SensorsService::class.java)
        context.stopService(intentMobile)
    }
}