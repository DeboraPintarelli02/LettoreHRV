import 'package:flutter/services.dart';
import 'package:mobile/domain/valore.dart';

class ValoriRepository {
  Future<List<Valore>> getValori(String dataset) async{
    
    List<Map>? valori = await MethodChannel("com.example.sensors/valori").invokeListMethod("getValori", {
      "dataset": dataset
    });

    return valori!.map((v)=>Valore(int.parse(v["codice"]), double.parse(v["valore"]), v["tipo"].toString(), DateTime.parse(v["momento"]))).toList();
  }
}