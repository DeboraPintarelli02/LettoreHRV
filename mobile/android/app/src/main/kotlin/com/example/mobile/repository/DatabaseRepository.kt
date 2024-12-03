package com.example.mobile.repository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull
import java.time.Instant

class DatabaseRepository(context: Context) : SQLiteOpenHelper(context, "sensoriDB", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
           CREATE TABLE IF NOT EXISTS dataset(
                nome varchar(100) not null PRIMARY KEY,
                dataCreazione datetime not null,
                selected BOOLEAN not null
           );
        """)

        db?.execSQL("""
           CREATE TABLE IF NOT EXISTS dato(
                codice integer PRIMARY KEY AUTOINCREMENT,
                valore REAL not null,
                momento datetime not null,
                tipo varchar(30) not null,
                dataset varchar(100) not null REFERENCES dataset(nome) ON DELETE CASCADE
           );
        """)
        
        db?.execSQL("PRAGMA foreign_keys = ON")

        db?.execSQL("INSERT INTO dataset (nome, dataCreazione, selected) VALUES ('default', '${Instant.now()}', true)")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun query(query: String, argomenti: Array<String>): ArrayList<Map<String, String>>{
        val db = readableDatabase

        val cursor: Cursor = db.rawQuery(query, argomenti)
        val righe: ArrayList<Map<String, String>> = ArrayList()

        cursor.move(1)
        while (!cursor.isAfterLast){
            val riga = HashMap<String, String>()
            for(colonna: String in cursor.columnNames){
                riga[colonna] = cursor.getStringOrNull(cursor.getColumnIndex(colonna)).toString()
            }

            cursor.move(1)
            righe.add(riga)
        }

        cursor.close()

        return righe;
    }

}