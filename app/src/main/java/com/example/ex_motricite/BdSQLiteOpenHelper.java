package com.example.ex_motricite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * The {@code BdSQLiteOpenHelper} class provides a helper for managing the SQLite database.
 * It extends {@link SQLiteOpenHelper} and is responsible for creating and upgrading the database.
 *
 * <p>
 * This class defines the structure of the database by creating tables for 'patient' and 'operator'.
 * </p>
 *
 * @author Ferreira, Rgouaud
 * @version 1.0
 */
public class BdSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * Constructs a new {@code BdSQLiteOpenHelper} with the specified context, database name, cursor factory,
     * and version.
     *
     * @param context   The application context.
     * @param name      The name of the database.
     * @param factory   The cursor factory (set to null by default).
     * @param version   The version of the database.
     */
    public BdSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Called when the database is created for the first time.
     * It defines the structure of the database by creating tables for 'patient' and 'operator'.
     *
     * @param db The database being created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the 'patient' table
        String tablePatient = "create table patient ("
                + "idPatient INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "firstName TEXT NOT NULL,"
                + "birthDate TEXT NOT NULL,"
                + "remarks TEXT NOT NULL);";
        db.execSQL(tablePatient);

        // Create the 'operator' table
        String tableOperator = "create table operator ("
                + "idOperator INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Name TEXT NOT NULL,"
                + "firstName TEXT NOT NULL);";
        db.execSQL(tableOperator);
    }

    /**
     * Called when the database needs to be upgraded.
     * Currently left empty as no database updates are planned.
     *
     * @param db         The database being upgraded.
     * @param oldVersion The old version of the database.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is left empty as currently no database updates are planned.
        // If updates are required in the future, they should be implemented here.
    }
}