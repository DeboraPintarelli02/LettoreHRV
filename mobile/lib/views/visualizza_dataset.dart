import 'package:file_selector/file_selector.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:mobile/domain/HRV.dart';
import 'package:mobile/domain/statistica.dart';
import 'package:mobile/domain/valore.dart';
import 'package:mobile/repository/local_repository.dart';
import 'package:mobile/repository/valori_repository.dart';

class VisualizzaDatasetView extends StatefulWidget {
  final String dataset;

  VisualizzaDatasetView(this.dataset, {super.key});

  @override
  State<VisualizzaDatasetView> createState() => _VisualizzaDatasetViewState();
}

class _VisualizzaDatasetViewState extends State<VisualizzaDatasetView> {
  final ValoriRepository repository = ValoriRepository();
  final LocalRepository localRepository = LocalRepository();

  List<FlSpot> getSposts(List<Valore> valori) {
    
    List<FlSpot> punti = [];
    for (int i = 0; i < valori.length; i++)
      punti.add(FlSpot(i.toDouble(), valori[i].valore));

    return punti;
  }
  

  Future<void> salvaDati(List<Valore> valori) async{
    if(valori.length == 0)
      return;

    String? directory = await getDirectoryPath();


    if(directory == null)
      return;
    
    String csv = "Codice;Momento;Valore;Tipo\n" + valori.map((v)=>"${v.codice};${v.momento.toString()};${v.valore};${v.tipo}\n").reduce((a, b)=> a + b);
    

    await localRepository.saveFile(directory, widget.dataset, "csv", csv);
  }

  String formattaData(DateTime? data) {
    if(data == null)
      return "";
    return "${data.day}/${data.month}/${data.year} ${data.hour.toString().padLeft(2, '0')}:${data.minute.toString().padLeft(2, "0")}";
  }


  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: SingleChildScrollView(
          child: Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            child: Column(
              children: [
                Container(
                    margin: EdgeInsets.only(top: 30),
                    child: Text(
                      widget.dataset,
                      style:
                          TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
                    )),
                Divider(),
                FutureBuilder(
                    future:
                        repository.getValori(widget.dataset),
                    builder: (context, snapshot) {
                      if (snapshot.hasData) {
                        List<Valore> valori = snapshot.data!;
                        Statistica statistica = HRV(valori).risultato;
                        return Column(
                          children: [
                            Text(
                              "Grafico",
                              style: TextStyle(fontSize: 30),
                            ),
                            SizedBox(
                              height: 300,
                              child: LineChart(
                                LineChartData(
                                  lineTouchData: LineTouchData(
                                    enabled: true,
                                  ),
                                  lineBarsData: [
                                LineChartBarData(
                                  spots: getSposts(valori),
                                ),
                              ])),
                            ),
                            Text("Statistiche", style: TextStyle(fontSize: 30)),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text(
                                  "HRV",
                                  style: TextStyle(fontSize: 25),
                                ),
                                Text(statistica.hrv?.toStringAsFixed(2) ?? "" + " bpm")
                              ],
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("Inizio",
                                  style: TextStyle(fontSize: 25),),
                                Text(formattaData(statistica.inizio))
                              ],
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("Fine",
                                  style: TextStyle(fontSize: 25),),
                                Text(formattaData(statistica.fine))
                              ],
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("Media",
                                  style: TextStyle(fontSize: 25),),
                                Text(statistica.media?.toStringAsFixed(2) ?? "" + " bpm")
                              ],
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("N Valori",
                                  style: TextStyle(fontSize: 25),),
                                Text(valori.length.toString())
                              ],
                            ),
                            TextButton(onPressed: ()=>salvaDati(valori), child: Text("Scarica CSV")),
                          ],
                        );
                      }
                      return Center(
                        child: CircularProgressIndicator(),
                      );
                    }),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
