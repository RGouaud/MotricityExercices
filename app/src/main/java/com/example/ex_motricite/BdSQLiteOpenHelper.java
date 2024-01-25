package com.example.ex_motricite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BdSQLiteOpenHelper extends SQLiteOpenHelper {


    public BdSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablePatient = "create table patient ("
                + "idPatient INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "firstName TEXT NOT NULL,"
                + "birthDate TEXT NOT NULL,"
                + "remarks TEXT NOT NULL);";
        db.execSQL(tablePatient);

        String tableOperator = "create table operator ("
                + "idOperator INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Name TEXT NOT NULL,"
                + "firstName TEXT NOT NULL);";
        db.execSQL(tableOperator);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is left empty as currently no database updates are planned.
        // If updates are required in the future, they should be implemented here.
    }


    }
