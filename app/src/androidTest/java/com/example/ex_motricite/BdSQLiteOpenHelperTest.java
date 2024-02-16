package com.example.ex_motricite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.ArrayList;

public class BdSQLiteOpenHelperTest {
    /**
     * Name of the database
     */
    static final String DB_NAME = "BDMotricity";
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
        String baseName = DB_NAME;
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
        String baseName = DB_NAME;
        int version = 1;

        String tableName = "Patient";
        String[] expectedColumns = {"idPatient", "name", "firstName", "birthDate", "remarks"};

        // WHEN
        BdSQLiteOpenHelper bd = new BdSQLiteOpenHelper(appContext, baseName, null, version);
        SQLiteDatabase db = bd.getWritableDatabase();

        // THEN
        // Check if the table exists
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        assertTrue("Table " + tableName + " does not exist", cursor.moveToFirst());

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
            assertTrue("Column " + expectedColumn + " does not exist in the table", columns.contains(expectedColumn));
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

        String tableName = "Operator";
        String[] expectedColumns = {"idOperator", "name", "firstName"};

        // WHEN
        BdSQLiteOpenHelper bd = new BdSQLiteOpenHelper(appContext, baseName, null, version);
        SQLiteDatabase db = bd.getWritableDatabase();

        // THEN
        // Check if the table exists
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        assertTrue("Table " + tableName + " does not exist", cursor.moveToFirst());

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
            assertTrue("Column " + expectedColumn + " does not exist in the table", columns.contains(expectedColumn));
        }

        db.close();
    }

    /**
     * Test to check if the table test is created
     * Check if the table exists and if the columns are correct
     */
    @Test
    public void testTableDeletedTestIsCreated() {
        // GIVEN
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String baseName = DB_NAME;
        int version = 1;

        String tableName = "DeletedTest";
        String[] expectedColumns = {"idTest", "path", "suppressionDate"};

        // WHEN
        BdSQLiteOpenHelper bd = new BdSQLiteOpenHelper(appContext, baseName, null, version);
        SQLiteDatabase db = bd.getWritableDatabase();

        // THEN
        // Check if the table exists
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        assertTrue("Table " + tableName + " does not exist", cursor.moveToFirst());

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
            assertTrue("Column " + expectedColumn + " does not exist in the table", columns.contains(expectedColumn));
        }

        db.close();
    }
}