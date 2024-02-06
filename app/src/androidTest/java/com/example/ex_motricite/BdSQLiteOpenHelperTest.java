package com.example.ex_motricite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BdSQLiteOpenHelperTest {

    /**
     * Default test to check if next tests should works or if there already are problems
     */
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.ex_motricite", appContext.getPackageName());
    }

    /**
     * Test to check if the database is created
     */
    @Test
    public void testDatabaseIsCreated(){
        //GIVEN
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String baseName = "accesBd";
        int version = 1;

        //WHEN
        BdSQLiteOpenHelper bd = new BdSQLiteOpenHelper(appContext, baseName, null, version);
        SQLiteDatabase db = bd.getWritableDatabase();

        //THEN
        assertTrue(db.isOpen());

        db.close();
    }

    /**
     * Test to check if the table patient is created
     * Check if the table exists and if the columns are correct
     */
    @Test
    public void testTablePatientIsCreated() {
        // GIVEN
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String baseName = "accesBd";
        int version = 1;

        String tableName = "patient";
        String[] expectedColumns = {"idPatient", "name", "firstName", "birthDate", "remarks"};

        // WHEN
        BdSQLiteOpenHelper bd = new BdSQLiteOpenHelper(appContext, baseName, null, version);
        SQLiteDatabase db = bd.getWritableDatabase();

        // THEN
        // Check if the table exists
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        assertTrue(cursor.moveToFirst(), "Table " + tableName + " does not exist");

        // Check if the columns exist in the table
        cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        ArrayList<String> columns = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String columnName = cursor.getString(cursor.getColumnIndex("name"));
                columns.add(columnName);
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (String expectedColumn : expectedColumns) {
            assertTrue(columns.contains(expectedColumn), "Column " + expectedColumn + " does not exist in the table");
        }

        db.close();
    }

    /**
     * Test to check if the table operator is created
     * Check if the table exists and if the columns are correct
     */
    @Test
    public void testTableOperatorIsCreated() {
        // GIVEN
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String baseName = "accesBd";
        int version = 1;

        String tableName = "operator";
        String[] expectedColumns = {"idOperator", "Name", "firstName"};

        // WHEN
        BdSQLiteOpenHelper bd = new BdSQLiteOpenHelper(appContext, baseName, null, version);
        SQLiteDatabase db = bd.getWritableDatabase();

        // THEN
        // Check if the table exists
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        assertTrue(cursor.moveToFirst(), "Table " + tableName + " does not exist");

        // Check if the columns exist in the table
        cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        ArrayList<String> columns = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String columnName = cursor.getString(cursor.getColumnIndex("name"));
                columns.add(columnName);
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (String expectedColumn : expectedColumns) {
            assertTrue(columns.contains(expectedColumn), "Column " + expectedColumn + " does not exist in the table");
        }

        db.close();
    }
}