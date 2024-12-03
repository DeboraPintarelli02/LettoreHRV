import 'package:flutter/material.dart';
import 'package:mobile/domain/dataset.dart';
import 'package:mobile/repository/dataset_repository.dart';
import 'package:mobile/views/components/dataset_comp.dart';
import 'package:mobile/views/crea_dataset.dart';
import 'package:mobile/views/visualizza_dataset.dart';

class ListaDataSetView extends StatefulWidget {
  const ListaDataSetView({super.key});

  @override
  State<ListaDataSetView> createState() => _ListaDatasetViewState();
}

class _ListaDatasetViewState extends State<ListaDataSetView> {
  String display = "Non ce niente";

  DatasetRepository datasetRepository = DatasetRepository();

  Future<void> apriCreaDataSet() async {
    await Navigator.push(
        context, MaterialPageRoute(builder: (context) => CreaDataSetView()));

    setState(() {});
  }

  Future<void> apriDataSet(Dataset dataset) async {
    await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => VisualizzaDatasetView(dataset.nome)));

    setState(() {});
  }

  Future<void> delete(Dataset dataset) async {
    await datasetRepository.deleteDataset(dataset);
    setState(() {});
  }

  Future<void> impostaDatasetDefault(Dataset dataset) async {
    await datasetRepository.impostaDatasetDefault(dataset);
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Container(
            margin: EdgeInsets.only(top: 30),
            child: Text(
              "Lista datasets",
              style: TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
            ),
          ),
          Divider(
            height: 50.0,
            indent: 20.0,
            endIndent: 20.0,
          ),
          Expanded(
            child: FutureBuilder(
              future: datasetRepository.getDatasets(),
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  List<Dataset> datasets = snapshot.data!;
                  return ListView.builder(
                    itemBuilder: (context, index) => DatasetComp(
                      datasets[index],
                      onDelete: () => delete(datasets[index]),
                      onTap: () => apriDataSet(datasets[index]),
                      onImpostaPredefinito: () => impostaDatasetDefault(datasets[index]),
                    ),
                    itemCount: datasets.length,
                  );
                } else
                  return Center(
                    child: CircularProgressIndicator(),
                  );
              },
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: apriCreaDataSet,
        backgroundColor: Colors.deepPurple,
        child: Icon(Icons.add),
      ),
    );
  }
}
