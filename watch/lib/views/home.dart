import 'package:flutter/material.dart';
import 'package:watch/domain/status.dart';
import 'package:watch/repository/servizio_repository.dart';

class HomeView extends StatefulWidget {
  const HomeView({super.key});

  @override
  State<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends State<HomeView> {
  final ServizioRepository repository = ServizioRepository();

  Future<Status> getStatus() async {
    return await repository.getStatus();
  }

  String isOkString(bool boolean) {
    return boolean ? "Ok" : "Non ok";
  }

  Future<void> attivaDisattivaServizio(Status status) async {
    if (status.servizio)
      await repository.stopService();
    else
      await repository.startService();

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: Center(
          child: FutureBuilder(
              future: getStatus(),
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  Status statusPermessi = snapshot.data!;
                  return Wrap(
                    children: [
                      Text(
                          "Status permessi: ${isOkString(statusPermessi.permessi)}"),
                      Text(
                          "Status servizio: ${isOkString(statusPermessi.servizio)}"),
                      TextButton(
                          onPressed: statusPermessi.permessi
                              ? () => attivaDisattivaServizio(statusPermessi)
                              : null,
                          child: Text(statusPermessi.servizio
                              ? "Disattiva servizio"
                              : "Attiva servizio")),
                      TextButton(
                          onPressed: () {
                            setState(() {});
                          },
                          child: Text("Aggiorna"))
                    ],
                  );
                } else
                  return CircularProgressIndicator();
              }),
        ),
      ),
    );
  }
}
