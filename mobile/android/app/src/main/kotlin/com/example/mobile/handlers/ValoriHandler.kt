package com.example.mobile.handlers

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.example.mobile.Listeners.GetValoriListener
import com.example.mobile.domain.HR
import com.example.mobile.domain.MessaggioDataLayer
import com.example.mobile.repository.DataLayerRepository
import com.example.mobile.repository.DatabaseRepository
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDate
import java.util.Timer


@Suppress("UNCHECKED_CAST")
class ValoriHandler(val context: Context): MethodChannel.MethodCallHandler {
    val dataLayerRepository: DataLayerRepository = DataLayerRepository(context)
    val db = DatabaseRepository(context)

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if(call.method == "getValori"){
            val request: Map<String, String> = call.arguments as Map<String, String>
            getValori(result, request.get("dataset")!!)
        }
    }

    fun getValori(result: MethodChannel.Result, dataset: String){

        val messageClient = Wearable.getMessageClient(context)
        var listener: GetValoriListener? = null

        val timer = object : CountDownTimer(5000, 5000) {
            override fun onTick(p0: Long) { }

            override fun onFinish() {
                if(listener != null)
                    messageClient.removeListener(listener!!)

                val valori = db.query("SELECT * FROM dato where dataset=?", arrayOf(dataset))
                result.success(valori)
            }
        }

        listener = GetValoriListener(context, result, timer, dataset)
        messageClient.addListener(listener)
        dataLayerRepository.sendData(MessaggioDataLayer("getValori", ""))

        timer.start()

    }
}