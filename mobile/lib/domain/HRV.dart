import 'dart:math';
import 'package:mobile/domain/statistica.dart';
import 'package:mobile/domain/valore.dart';

class HRV {
  Statistica? _risultato;

  HRV(List<Valore> valori) {
    _risultato = _calcolaRisultato(valori);
  }

  Statistica _calcolaRisultato(List<Valore> valori) {
    if(valori.length == 0)
      return Statistica(null, null, null, null);

    List<double> heartRates = valori.map((v) => v.valore).toList();

    double media = heartRates.reduce((a, b)=> a + b) / heartRates.length;

    double hrv = sqrt(heartRates.map((v)=>pow(v - media, 2)).reduce((a, b)=> a+b) / (heartRates.length - 1));

    DateTime inizio = valori.map((e)=>e.momento).reduce((a, b)=>a.compareTo(b) > 0 ? b : a);
    DateTime fine = valori.map((e)=>e.momento).reduce((a, b)=>a.compareTo(b) > 0 ? a : b);
    
    return Statistica(media, hrv, inizio, fine);
  }

  Statistica get risultato {
    return _risultato!;
  }
}
