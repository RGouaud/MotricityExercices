package com.example.ex_motricite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BdSQLiteOpenHelper extends SQLiteOpenHelper {

    private String table_patient="create table patient ("
            + "idPatient INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"name TEXT NOT NULL,"
            +"firstName TEXT NOT NULL,"
            +"birthDate TEXT NOT NULL,"
            +"remarks TEXT NOT NULL);";

    private String table_operator="create table operator ("
            + "idOperator INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"Name TEXT NOT NULL,"
            +"firstName TEXT NOT NULL);";


    public BdSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table_patient);
        db.execSQL(table_operator);

        db.execSQL("insert into operator (Name, firstName) values('Hentrics', 'Samuel')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    }
