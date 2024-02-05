package com.example.ex_motricite;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;

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
}