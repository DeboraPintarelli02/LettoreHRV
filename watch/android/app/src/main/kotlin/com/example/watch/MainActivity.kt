package com.example.watch

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PersistableBundle
import com.example.watch.manager.ClockManager
import com.example.watch.receivers.DatiReceiver
import com.example.watch.services.MobileService
import com.example.watch.services.SensorsService
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import kotlin.concurrent.thread


class MainActivity: FlutterActivity(), MethodCallHandler{

    private val clockManager = ClockManager(this)

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val binaryMessenger =flutterEngine.dartExecutor.binaryMessenger
        MethodChannel(binaryMessenger, "com.example.sensors/service").setMethodCallHandler(this)

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if(call.method == "status")
            result.success(clockManager.checkStatus())

        if(call.method == "startService"){

            clockManager.startClock()

            val sharedPreferences = getSharedPreferences("servizio", Context.MODE_PRIVATE).edit()
            sharedPreferences.putBoolean("status", true)
            sharedPreferences.apply()

            result.success(null)
        }

        if(call.method == "stopService"){

            clockManager.stopService()

            val sharedPreferences = getSharedPreferences("servizio", Context.MODE_PRIVATE).edit()
            sharedPreferences.putBoolean("status", false)
            sharedPreferences.apply()

            result.success(null)
        }
    }
}
