import 'package:flutter/services.dart';

class StatusRepository {
  Future<bool> getStatus() async{
    return await MethodChannel("com.example.sensors/status").invokeMethod("getStatus");
  }

  Future<void> startServizio() async{
    await MethodChannel("com.example.sensors/status").invokeMethod("startService");
  }

  Future<void> stopServizio() async{
    await MethodChannel("com.example.sensors/status").invokeMethod("stopService");
  }
}