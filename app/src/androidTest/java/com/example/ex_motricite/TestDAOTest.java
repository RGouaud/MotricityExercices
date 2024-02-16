package com.example.ex_motricite;

import static org.junit.Assert.assertEquals;

import static java.lang.Integer.parseInt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;

public class TestDAOTest {

    /**
     * Instance of the testDAO to be tested
     */
    private TestDAO theTestDAO;

    /**
     * The tes.
     */
    private com.example.ex_motricite.Test test;

    /**
     * The Retrieved test.
     */
    private com.example.ex_motricite.Test retrievedTest;

    /**
     * The Id test.
     */
    private int idTest;

    /**
     * Instance to access the database
     */
    private BdSQLiteOpenHelper accesBD;

    /**
     * The context of the application
     */
    private Context ct;

    /**
     * The List patients.
     */
    private ArrayList<Test> listTests;

    /**
     * The Curseur to store the result of a query
     */
    private Cursor curseur;

    /**
     * Set up all the variables needed for the tests
     */

    @Before
    void setUp(){
        ct = InstrumentationRegistry.getInstrumentation().getTargetContext();
        theTestDAO = new TestDAO(ct);
        test = new Test( "/directory/file", "01/01/2000";
        retrievedTest = null;
        idTest = 0;
        accesBD = new BdSQLiteOpenHelper(ct, "BDMotricity", null, 1);
        listTests = new ArrayList<>();
    }
    /**
     * Close the database and set all the variables to null
     */
    @After
    public void tearDown(){
        accesBD.close();
        theTestDAO = null;
        test = null;
        retrievedTest = null;
        idTest = 0;
        listTests = null;
        accesBD = null;
    }

    /**
     * Test getPatient where id is registered.
     */
    @org.junit.Test
    public void testGetPatientWhereIdIsRegistered() {
        // GIVEN
        ContentValues values = new ContentValues();
        values.put("suppressionDate", test.getSuppressionDate());
        values.put("path", test.getPath());
        idTest = accesBD.getWritableDatabase().insert("test", null, values);

        // WHEN
        retrievedTest = theTestDAO.getTest(idTest);

        // THEN
        assertEquals("Patient name does not match", patient.getName(), retrievedPatient.getName());
        assertEquals("Patient first name does not match", patient.getFirstName(), retrievedPatient.getFirstName());
        assertEquals("Patient birth date does not match", patient.getBirthDate(), retrievedPatient.getBirthDate());
        accesBD.getReadableDatabase().execSQL("delete from patient where idPatient="+idPatient+";");
    }

}