package com.example.watch.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.example.watch.R
import com.example.watch.Repository.DatabaseRepository
import com.example.watch.data.MessaggioDataLayer
import com.example.watch.data.MessaggioEliminazioneValori
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import java.nio.charset.StandardCharsets
import java.time.Instant

class SensorsService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var hrvSensor: Sensor
    private val repository = DatabaseRepository.getInstace(this)

    companion object{
        var status = false
    }

    override fun onCreate() {
        super.onCreate()

        configureBodySensor()
        status = true
    }



    private fun configureBodySensor(){
        sensorManager = this.getSystemService(SENSOR_SERVICE) as SensorManager
        hrvSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) as Sensor

        sensorManager.registerListener(this, hrvSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onSensorChanged(data: SensorEvent?) {
        val hrv: Float = data!!.values[0]
        val hrvString = hrv.toString()

        if(hrvString != "0.0"){
            repository.query("INSERT INTO dato (valore, momento, tipo) VALUES (?, ?, ?)", arrayOf(
                hrvString,
                Instant.now().toString(),
                "HR"
            ))

            val intent = Intent(this, SensorsService::class.java)
            stopService(intent)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onDestroy() {
        super.onDestroy()

        sensorManager.unregisterListener(this, hrvSensor)
        //Wearable.getMessageClient(this).removeListener(this)

        status = false
    }
}