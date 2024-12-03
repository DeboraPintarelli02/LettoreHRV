import 'dart:io';

import 'package:path/path.dart';

class LocalRepository {
  Future<void> saveFile(String directory, String nome, String estensione, String content) async{
    
    String path;
    File file;
    int i = 0;
    do{
      path = join(directory, i == 0 ? "${nome}.${estensione}" : "${nome}(${i}).${estensione}");
      file = File(path);

      i++;
    }while(await file.exists());

    await file.writeAsString(content);
  }
}