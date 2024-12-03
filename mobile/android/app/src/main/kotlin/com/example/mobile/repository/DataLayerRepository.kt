package com.example.mobile.repository

import android.content.Context
import com.example.mobile.domain.MessaggioDataLayer
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson

class DataLayerRepository(val context: Context) {
    fun sendData(data: MessaggioDataLayer){
        Wearable.getCapabilityClient(this.context).getCapability("sensors_data", CapabilityClient.FILTER_REACHABLE).addOnSuccessListener {
                capabilityInfo: CapabilityInfo? ->

            val json = Gson().toJson(data)
            for(node: Node in capabilityInfo!!.nodes){
                Wearable.getMessageClient(this.context).sendMessage(node.id, "sensors_data", json.toByteArray())
            }
        }
    }
}