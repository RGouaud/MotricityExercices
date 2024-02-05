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
}