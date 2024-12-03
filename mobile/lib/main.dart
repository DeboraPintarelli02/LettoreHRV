import 'dart:async';

import 'package:flutter/material.dart';
import 'package:mobile/views/home.dart';


Future<void> main() async {
  runApp(const App());
}

class App extends StatelessWidget {
  static final navigatorKey = GlobalKey<NavigatorState>();

  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData.dark(),
      home: HomeView(),
      navigatorKey: navigatorKey,
    );
  }
}
