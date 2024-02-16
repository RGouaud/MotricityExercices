package com.example.ex_motricite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class DeletedTestDAOTest {

    /**
     * Instance of the DeletedTestDAO to be tested
     */
    private DeletedTestDAO deletedTestDAO;
    /**
     * The DeletedTest.
     */
    private DeletedTest deletedTest;
    /**
     * The Retrieved deletedTest.
     */
    private DeletedTest retrievedDeletedTest;
    /**
     * The Id deletedTest.
     */
    private long idDeletedTest;
    /**
     * Instance to access the database
     */
    private BdSQLiteOpenHelper accesBD;
    /**
     * The List of deleted tests.
     */
    private ArrayList<DeletedTest> listDeletedTests;

    /**
     * The Cursor to store the result of a query
     */
    private Cursor cursor;

    /**
     * Set up all variables needed for the tests
     */
    @Before
    public void setUp() {
        /**
         * The context of the application.
         */
        Context ct = InstrumentationRegistry.getInstrumentation().getTargetContext();
        deletedTestDAO = new DeletedTestDAO(ct);
        deletedTest = new DeletedTest( "/directory/path/", "29/11/2003");
        retrievedDeletedTest = null;
        idDeletedTest = 0;
        accesBD = new BdSQLiteOpenHelper(ct, "BDMotricity", null, 1);
        listDeletedTests = new ArrayList<>();
    }

    /**
     * Close the database and set all the variables to null
     */
    @After
    public void tearDown() {
        deletedTestDAO = null;
        deletedTest = null;
        retrievedDeletedTest = null;
        idDeletedTest = 0;
        accesBD = null;
        listDeletedTests = null;
    }

    /**
     * Test get deletedTest where id is registered.
     */
    @Test
    public void testGetDeletedTestWhereIdIsRegistered(){
        // GIVEN
        ContentValues values = new ContentValues();
        values.put("path", deletedTest.getPath());
        values.put("suppressionDate", deletedTest.getSuppressionDate());
        idDeletedTest = accesBD.getWritableDatabase().insert("DeletedTest", null, values);

        // WHEN
        retrievedDeletedTest = deletedTestDAO.getTest(idDeletedTest);

        // THEN
        assertEquals("Test path does not match", "/directory/path/", retrievedDeletedTest.getPath());
        assertEquals("Test suppression date does not match", "29/11/2003", retrievedDeletedTest.getSuppressionDate());
        accesBD.getWritableDatabase().execSQL("delete from DeletedTest where idTest="+ idDeletedTest +";");
    }

    /**
     * Test getDeletedTest where id is not registered.
     */
    @Test
    public void testGetOperatorWhereIdIsNotRegistered() {
        // GIVEN
        idDeletedTest = 0;

        // WHEN
        retrievedDeletedTest = deletedTestDAO.getTest(idDeletedTest);

        // THEN
        assertNull("DeletedTest is not null", retrievedDeletedTest);
    }

    /**
     * Test getDeletedTest where id is not registered.
     */
    @Test
    public void testGetTestWhereIdIsNotRegistered() {
        // GIVEN

        // WHEN
        DeletedTest retrievedTest = deletedTestDAO.getTest(100);

        // THEN
        assertNull("Retrieved test is not null", retrievedTest);
    }

    /**
     * Test add deletedTest.
     */
    @Test
    public void testAddDeletedTest() {
        //GIVEN

        //WHEN
        idDeletedTest = deletedTestDAO.addTest(deletedTest);

        //THEN
        cursor = accesBD.getReadableDatabase().rawQuery("select * from DeletedTest where idTest="+ idDeletedTest +";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            retrievedDeletedTest = new DeletedTest(idDeletedTest, cursor.getString(1),cursor.getString(2));
        }
        assertEquals("Operator name does not match", deletedTest.getPath(), retrievedDeletedTest.getPath());
        assertEquals("Operator first name does not match", deletedTest.getSuppressionDate(), retrievedDeletedTest.getSuppressionDate());
        accesBD.getReadableDatabase().execSQL("delete from DeletedTest where idTest="+ idDeletedTest +";");
    }

    /**
     * Test delete deletedTest.
     */
    @Test
    public void testDeleteTest() {
        //GIVEN
        ContentValues values = new ContentValues();
        values.put("path", deletedTest.getPath());
        values.put("suppressionDate", deletedTest.getSuppressionDate());
        idDeletedTest = accesBD.getWritableDatabase().insert("DeletedTest", null, values);

        cursor = accesBD.getReadableDatabase().rawQuery("select * from DeletedTest where idTest="+ idDeletedTest +";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            retrievedDeletedTest = new DeletedTest(idDeletedTest, cursor.getString(1),cursor.getString(2));
        }

        //WHEN
        deletedTestDAO.delTest(retrievedDeletedTest);

        //THEN
        cursor = accesBD.getReadableDatabase().rawQuery("select * from DeletedTest where idTest="+idDeletedTest+";",null);
        try {
            assertNotNull(cursor);
            assertEquals("Test not deleted", 0, cursor.getCount());

            //if the Test is not deleted, we delete it
            if (cursor.getCount() > 0) {
                accesBD.getReadableDatabase().execSQL("delete from DeletedTest where idTest=" + idDeletedTest + ";");
            }
        }finally {
            cursor.close();
        }
    }

    /**
     * Test getAllTests.
     */
    @Test
    public void testGetAllTests() {
        //GIVEN

        //WHEN
        // Using getAllTests method to get all operators
        listDeletedTests = deletedTestDAO.getAllTests();

        //THEN
        // select all operators in SQL query
        cursor = accesBD.getReadableDatabase().rawQuery("select * from operator;",null);
        // compare the size of the sql query and the size of the list, they must be equal, then we consider that it is good
        assertEquals("List size does not match", cursor.getCount(), listDeletedTests.size());
        // close the cursor
        cursor.close();
    }
}
