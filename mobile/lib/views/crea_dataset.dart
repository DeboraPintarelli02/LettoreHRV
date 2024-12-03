import 'package:flutter/material.dart';
import 'package:mobile/domain/dataset.dart';
import 'package:mobile/repository/dataset_repository.dart';

class CreaDataSetView extends StatefulWidget {
  const CreaDataSetView({super.key});

  @override
  State<CreaDataSetView> createState() => _CreaDataSetViewState();
}

class _CreaDataSetViewState extends State<CreaDataSetView> {
  TextEditingController txtNome = TextEditingController();
  DatasetRepository repository = DatasetRepository();
  String? errore = null;

  Future<void> inserisci() async {
    try {
      Dataset dataset = new Dataset(txtNome.text, DateTime.now(), false);

      await repository.insertDataset(dataset);

      Navigator.pop(context);
    } catch (err) {
      setState(() {
        errore = "Nome dataset già usato";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: Container(
          margin: EdgeInsets.symmetric(horizontal: 50),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                "Crea un nuovo dataset",
                style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
                textAlign: TextAlign.center,
              ),
              SizedBox(
                height: 30,
              ),
              Text(errore ?? ""),
              TextField(
                decoration: InputDecoration(hintText: "Inserisci il nome"),
                controller: txtNome,
              ),
              SizedBox(
                height: 10,
              ),
              TextButton(
                  onPressed: inserisci,
                  child: Text(
                    "Inserisci",
                    style: TextStyle(fontSize: 20),
                  ))
            ],
          ),
        ),
      ),
    );
  }
}
