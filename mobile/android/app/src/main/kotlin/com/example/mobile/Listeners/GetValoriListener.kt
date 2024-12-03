package com.example.mobile.Listeners

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import com.example.mobile.domain.MessaggioDataLayer
import com.example.mobile.domain.MessaggioEliminazioneValori
import com.example.mobile.repository.DataLayerRepository
import com.example.mobile.repository.DatabaseRepository
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.flutter.plugin.common.MethodChannel
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.util.Date

class GetValoriListener(val context: Context, val result: MethodChannel.Result, val timer: CountDownTimer, val datasetSelezionato: String): MessageClient.OnMessageReceivedListener {

    private val db = DatabaseRepository(context)
    private val dataLayerRepository = DataLayerRepository(context)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        timer.cancel()

        val messaggio = Gson().fromJson(String(messageEvent.data, StandardCharsets.UTF_8), MessaggioDataLayer::class.java)
        val type = object : TypeToken<ArrayList<Map<String, String>>>(){}.type
        val valori: ArrayList<Map<String, String>> = Gson().fromJson(messaggio.json, type)

        val nomeDataset = db.query("SELECT nome FROM dataset WHERE selected=true", arrayOf()).get(0).get("nome")

        for (valore: Map<String, String> in valori){
            db.query("INSERT INTO dato (valore, momento, tipo, dataset) VALUES (?, ?, ?, ?)", arrayOf(
                valore.get("valore").toString(),
                valore.get("momento").toString(),
                "HR",
                nomeDataset.toString()
            ))
        }

        val ultimoValore = db.query("SELECT MAX(momento) as n FROM dato", arrayOf()).get(0).get("n")

        val messaggioEliminazione: MessaggioDataLayer = MessaggioDataLayer("eliminaValori", Gson().toJson(MessaggioEliminazioneValori(ultimoValore.toString())))

        dataLayerRepository.sendData(messaggioEliminazione)

        val nuoviValori = db.query("SELECT * FROM dato where dataset=?", arrayOf(datasetSelezionato))

        Wearable.getMessageClient(context).removeListener(this)

        result.success(nuoviValori)
        Wearable.getMessageClient(this.context)
    }
}