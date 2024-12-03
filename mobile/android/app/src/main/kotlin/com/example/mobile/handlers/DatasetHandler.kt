package com.example.mobile.handlers

import android.content.Context
import com.example.mobile.repository.DatabaseRepository
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

@Suppress("UNCHECKED_CAST")
class DatasetHandler(context: Context) : MethodCallHandler {

    private val databaseRepository = DatabaseRepository(context)

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if(call.method == "getDatasets"){
            getDataset(result)
        }

        if(call.method == "insertDataset"){
            val dataset: Map<String, Any> = call.arguments as Map<String, Any>
            insertDataset(result, dataset)
        }

        if(call.method == "deleteDataset"){
            deleteDataset(result, call.arguments as String)
        }

        if(call.method == "setDefault"){
            impostaDatasetDefault(result, call.arguments.toString())
        }
    }


    private fun impostaDatasetDefault(result: MethodChannel.Result, dataset: String){
        databaseRepository.query("UPDATE dataset SET selected=false WHERE selected=true", arrayOf())
        databaseRepository.query("UPDATE dataset SET selected=true WHERE nome=?", arrayOf(dataset))
        result.success(null)
    }

    private fun getDataset(result: MethodChannel.Result){
        val data = databaseRepository.query("SELECT * FROM dataset", arrayOf())
        result.success(data)
    }

    private fun insertDataset(result: MethodChannel.Result, dataset: Map<String, Any>){
        databaseRepository.query("INSERT INTO dataset (nome, dataCreazione, selected) VALUES (?, ?, ?)", arrayOf(dataset["nome"].toString(), dataset["dataCreazione"].toString(), dataset["selected"].toString()))
        result.success(null);
    }

    private fun deleteDataset(result: MethodChannel.Result, nomeDataset: String){
        databaseRepository.query("DELETE FROM dataset where nome=?", arrayOf(nomeDataset))
        databaseRepository.query("DELETE FROM dato where dataset=?", arrayOf(nomeDataset))
        result.success(null)
    }
}