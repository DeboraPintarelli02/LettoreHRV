import 'package:flutter/services.dart';
import 'package:mobile/domain/dataset.dart';
class DatasetRepository {

  Future<List<Dataset>> getDatasets() async{
    /*
    List<Map<String, Object?>> rows = await _db.loadData("dataset");
    
    return rows.map((Map dato)=>Dataset(dato["nome"], DateTime.parse(dato["dataCreazione"]))).toList();
    */

    List<Object?> rows = await MethodChannel("com.example.sensors/datasets").invokeMethod("getDatasets") ?? [];
    return rows.map((dynamic dato)=>Dataset(dato["nome"], DateTime.parse(dato["dataCreazione"]), dato["selected"] == '1')).toList();
  }

  Future<void> insertDataset(Dataset dataset) async{
    await MethodChannel("com.example.sensors/datasets").invokeListMethod("insertDataset", {
      "nome": dataset.nome,
      "dataCreazione": dataset.dataCreazione.toString(),
      "selected": dataset.selected
    });
  }

  Future<void> deleteDataset(Dataset dataset) async{
    await MethodChannel("com.example.sensors/datasets").invokeMethod("deleteDataset", dataset.nome);
  }

  Future<void> impostaDatasetDefault(Dataset dataset) async{
    await MethodChannel("com.example.sensors/datasets").invokeMethod("setDefault", dataset.nome);
  }
}