package com.example.mobile

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mobile.handlers.DatasetHandler
import com.example.mobile.handlers.ValoriHandler
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMethodCodec

class MainActivity: FlutterActivity() {
    private lateinit var dataSetChannel: MethodChannel
    private lateinit var valoriChannel: MethodChannel
    private lateinit var statusChannel: MethodChannel

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val messeger: BinaryMessenger = flutterEngine.dartExecutor.binaryMessenger

        dataSetChannel = MethodChannel(messeger, "com.example.sensors/datasets", StandardMethodCodec.INSTANCE)
        dataSetChannel.setMethodCallHandler(DatasetHandler(this))

        valoriChannel = MethodChannel(messeger,"com.example.sensors/valori", StandardMethodCodec.INSTANCE)
        valoriChannel.setMethodCallHandler(ValoriHandler(this))


    }


}
