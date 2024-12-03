package com.example.watch.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
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

class MobileService : Service(), MessageClient.OnMessageReceivedListener{

    companion object{
        var status: Boolean = false
    }

    private val repository = DatabaseRepository.getInstace(this)

    override fun onCreate() {
        super.onCreate()

        status = true

        Wearable
            .getMessageClient(this)
            .addListener(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun sendData(data: MessaggioDataLayer){
        Wearable.getCapabilityClient(this).getCapability("sensors_data", CapabilityClient.FILTER_REACHABLE).addOnSuccessListener {
                capabilityInfo: CapabilityInfo? ->

            val json = Gson().toJson(data)
            for(node: Node in capabilityInfo!!.nodes){
                Wearable.getMessageClient(this).sendMessage(node.id, "sensors_data", json.toByteArray())
            }
        }
    }

    override fun onMessageReceived(event: MessageEvent) {
        val serializer = Gson()

        val messaggio: MessaggioDataLayer = serializer.fromJson(String(event.data, StandardCharsets.UTF_8), MessaggioDataLayer::class.java)

        if(messaggio.canale == "getValori"){
            val dati = repository.query("SELECT valore, momento FROM dato", arrayOf())
            val ris = MessaggioDataLayer("getValori", serializer.toJson(dati))

            sendData(ris)
        }

        if(messaggio.canale == "eliminaValori"){
            val req: MessaggioEliminazioneValori = serializer.fromJson(messaggio.json, MessaggioEliminazioneValori::class.java)
            repository.query("DELETE FROM dato WHERE momento <= ?", arrayOf(req.momento))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        status = false
    }
}