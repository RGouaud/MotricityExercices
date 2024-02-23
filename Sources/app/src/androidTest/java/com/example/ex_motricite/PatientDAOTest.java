package com.example.ex_motricite;

import static org.junit.Assert.*;

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
public class PatientDAOTest {
    /**
     * Instance of the PatientDAO to be tested
     */
    private PatientDAO patientDAO;

    /**
     * The Patient.
     */
    private Patient patient;

    /**
     * The Retrieved patient.
     */
    private  Patient retrievedPatient;

    /**
     * The Id patient.
     */
    private long idPatient;

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
    private ArrayList<Patient> listPatients;

    /**
     * The Curseur to store the result of a query
     */
    private Cursor curseur;

    /**
     * Set up all the variables needed for the tests
     */
    @Before
    public void setUp(){
        ct = InstrumentationRegistry.getInstrumentation().getTargetContext();
        patientDAO = new PatientDAO(ct);
        patient = new Patient( "name", "firstName", "19/03/2001", "remarks");
        retrievedPatient = null;
        idPatient = 0;
        accesBD = new BdSQLiteOpenHelper(ct, "BDMotricity", null, 1);
        listPatients = new ArrayList<>();
    }

    /**
     * Close the database and set all the variables to null
     */
    @After
    public void tearDown(){
        accesBD.close();
        patientDAO = null;
        patient = null;
        retrievedPatient = null;
        idPatient = 0;
        listPatients = null;
        accesBD = null;
    }

    /**
     * Test getPatient where id is registered.
     */
    @Test
    public void test_get_patient_where_id_is_registered() {
        // GIVEN
        ContentValues values = new ContentValues();
        values.put("name", patient.getName());
        values.put("firstName", patient.getFirstName());
        values.put("birthDate", patient.getBirthDate());
        values.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("patient", null, values);

        // WHEN
        retrievedPatient = patientDAO.getPatient(idPatient);

        // THEN
        assertEquals("Patient name does not match", patient.getName(), retrievedPatient.getName());
        assertEquals("Patient first name does not match", patient.getFirstName(), retrievedPatient.getFirstName());
        assertEquals("Patient birth date does not match", patient.getBirthDate(), retrievedPatient.getBirthDate());
        accesBD.getReadableDatabase().execSQL("delete from patient where idPatient="+idPatient+";");
    }

    /**
     * Test getPatient where id is not registered.
     */
    @Test
    public void test_get_patient_where_id_is_not_registered() {
        // GIVEN
        idPatient = 0;

        // WHEN
        retrievedPatient = patientDAO.getPatient(idPatient);

        // THEN
        assertNull("Patient is not null", retrievedPatient);
    }


    /**
     * Test add patient.
     */
    @Test
    public void test_add_patient() {
        //GIVEN

        //WHEN
        idPatient = patientDAO.addPatient(patient);

        //THEN
        curseur = accesBD.getReadableDatabase().rawQuery("select * from patient where idPatient="+ idPatient +";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            retrievedPatient = new Patient(idPatient, curseur.getString(1),curseur.getString(2), curseur.getString(3), curseur.getString(4));
        }
        assertEquals("Patient name does not match", patient.getName(), retrievedPatient.getName());
        assertEquals("Patient first name does not match", patient.getFirstName(), retrievedPatient.getFirstName());
        assertEquals("Patient birth date does not match", patient.getBirthDate(), retrievedPatient.getBirthDate());
        accesBD.getReadableDatabase().execSQL("delete from patient where idPatient="+idPatient+";");
    }

    /**
     * Test delete patient.
     */
    @Test
    public void test_del_patient() {
        //GIVEN
        ContentValues values = new ContentValues();
        values.put("name", patient.getName());
        values.put("firstName", patient.getFirstName());
        values.put("birthDate", patient.getBirthDate());
        values.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("patient", null, values);

        curseur = accesBD.getReadableDatabase().rawQuery("select * from patient where idPatient="+ idPatient +";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            retrievedPatient = new Patient(idPatient, curseur.getString(1),curseur.getString(2) ,curseur.getString(3), curseur.getString(4));
        }

        //WHEN
        patientDAO.delPatient(retrievedPatient);

        //THEN
        curseur = accesBD.getReadableDatabase().rawQuery("select * from patient where idPatient="+ idPatient +";",null);
        try {
            assertNotNull(curseur);
            assertEquals("Patient not deleted", 0, curseur.getCount());

            //if the patient is not deleted, we delete it
            if (curseur.getCount() > 0) {
                accesBD.getReadableDatabase().execSQL("delete from patient where idPatient=" + idPatient + ";");
            }
        }finally {
            curseur.close();
        }
    }

    /**
     * Test get patients.
     */
    @Test
    public void test_getPatients() {
        //GIVEN

        //WHEN
        listPatients = patientDAO.getPatients();

        //THEN
        curseur = accesBD.getReadableDatabase().rawQuery("select * from patient;",null);
        listPatients = patientDAO.getPatients();
        assertEquals("List size does not match", curseur.getCount(), listPatients.size());
        curseur.close();
    }

    /**
     * Test update patient.
     */
    @Test
    public void test_update_patient() {
        //GIVEN
        ContentValues values = new ContentValues();
        values.put("name", patient.getName());
        values.put("firstName", patient.getFirstName());
        values.put("birthDate", patient.getBirthDate());
        values.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("patient", null, values);

        //WHEN
        retrievedPatient = patientDAO.getPatient(idPatient);
        retrievedPatient.setName("newName");
        retrievedPatient.setFirstName("newFirstName");
        retrievedPatient.setBirthDate("19/03/2002");
        patientDAO.updatePatient(retrievedPatient);

        //THEN
        curseur = accesBD.getReadableDatabase().rawQuery("select * from patient where idPatient="+ idPatient +";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            retrievedPatient = new Patient(idPatient, curseur.getString(1),curseur.getString(2) ,curseur.getString(3), curseur.getString(4));
        }
        assertEquals("Patient name does not match", "newName", retrievedPatient.getName());
        assertEquals("Patient first name does not match", "newFirstName", retrievedPatient.getFirstName());
        assertEquals("Patient birth date does not match", "19/03/2002", retrievedPatient.getBirthDate());
        accesBD.getReadableDatabase().execSQL("delete from patient where idPatient="+idPatient+";");
    }

}