import 'package:flutter/material.dart';
import 'package:mobile/domain/dataset.dart';

class DatasetComp extends StatelessWidget {
  final Dataset dataset;
  final VoidCallback? onDelete;
  final VoidCallback? onTap;
  final VoidCallback? onImpostaPredefinito;
  final VoidCallback? onUpload;

  const DatasetComp(this.dataset, {super.key, this.onDelete, this.onTap, this.onImpostaPredefinito, this.onUpload});

  String formattaData() {
    DateTime dateTime = dataset.dataCreazione;

    return "${dateTime.day}/${dateTime.month}/${dateTime.year} ${dateTime.hour.toString().padLeft(2, '0')}:${dateTime.minute.toString().padLeft(2, "0")}";
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: onTap,
      title: Text(dataset.nome),
      subtitle: Text(formattaData()),
      trailing: PopupMenuButton(itemBuilder: (context){
        return [
          if(!dataset.selected)
            PopupMenuItem(child: Text("Elimina"), onTap: onDelete,),
          if(!dataset.selected)
            PopupMenuItem(child: Text("Imposta predefinito"), onTap: onImpostaPredefinito),
        ];
      }));
  }
}
