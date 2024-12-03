package com.example.watch.Repository

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull
import java.time.Instant

class DatabaseRepository private constructor(context: Context) :
    SQLiteOpenHelper(context, "sensoriDB", null, 1) {


    companion object{
        private var instance: DatabaseRepository? = null

        fun getInstace(context: Context): DatabaseRepository{
            if(this.instance == null)
                this.instance = DatabaseRepository(context)

            return instance!!
        }

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
           CREATE TABLE IF NOT EXISTS dato(
                codice integer PRIMARY KEY AUTOINCREMENT,
                valore REAL not null,
                momento datetime not null,
                tipo varchar(30) not null
           );
        """)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun query(query: String, argomenti: Array<String>): List<Map<String, String>>{
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