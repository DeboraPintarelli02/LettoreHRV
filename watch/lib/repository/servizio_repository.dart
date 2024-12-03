import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:watch/domain/status.dart';

class ServizioRepository {
  Future<Status> getStatus() async{
    bool servizio = await MethodChannel("com.example.sensors/service").invokeMethod("status");
    bool permessi = await Permission.sensors.isGranted;

    return Status(servizio, permessi);
  }

  Future startService() async{
    await MethodChannel("com.example.sensors/service").invokeMethod("startService");
  }

  Future stopService() async{
    await MethodChannel("com.example.sensors/service").invokeMethod("stopService");
  }
}