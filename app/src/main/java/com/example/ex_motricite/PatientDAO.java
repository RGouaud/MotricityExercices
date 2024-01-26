package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * The {@code PatientDAO} class represents a DAO (Data Access Object) for patients.
 *
 * <p>
 * This class is used to retrieve patient data from a database. It provides methods to retrieve, add, delete, and update
 * patient data in the database.
 * </p>
 *
 * <p>
 *     Author: Ferreria, Rgouaud
 *     Version: 1.0
 * </p>
 */
public class PatientDAO {
    private static final String BASE = "BDMotricity";
    private static final int VERSION = 1;
    private final BdSQLiteOpenHelper bdAccess;

    /**
     * Constructor for PatientDAO class.
     *
     * @param ct Android application context.
     */
    public PatientDAO(Context ct) {
        bdAccess = new BdSQLiteOpenHelper(ct, BASE, null, VERSION);
    }

    /**
     * Retrieves a patient from the database based on the provided patient ID.
     *
     * @param idPatient The ID of the patient to retrieve.
     * @return A {@code Patient} object representing the retrieved patient; {@code null} if not found.
     */
    public Patient getPatient(long idPatient){
        Patient patient = null;
        try(Cursor cursor = bdAccess.getReadableDatabase().rawQuery("select * from patient where idPatient="+idPatient+";",null))
        {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                patient = new Patient(idPatient, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            }
        }
        return patient;
    }

    /**
     * Adds a new patient to the database.
            *
            * @param patient The {@code Patient} object representing the patient to be added.
     */
    public void addPatient(Patient patient){
        bdAccess.getWritableDatabase().execSQL("INSERT INTO patient (name, firstName, birthDate, remarks) VALUES ('" + patient.getName() + "','" + patient.getFirstName() + "','" + patient.getBirthDate() + "','" + patient.getRemarks() + "');");
        bdAccess.close();
    }

    /**
     * Deletes a patient from the database.
     *
     * @param patient The {@code Patient} object representing the patient to be deleted.
     */
    public void delPatient(Patient patient){
        bdAccess.getWritableDatabase().execSQL("delete from patient where idPatient="+patient.getId()+";");
        bdAccess.close();
    }

    /**
     * Retrieves a list of all patients from the database.
     *
     * @return An {@code ArrayList<Patient>} containing all patients in the database.
     */
    public ArrayList<Patient> getPatients(){
        Cursor cursor;
        String req = "select * from patient;";
        cursor = bdAccess.getReadableDatabase().rawQuery(req,null);
        return cursorToPatientArrayList(cursor);
    }

    /**
     * Updates an existing patient in the database.
     *
     * @param patient The {@code Patient} object representing the updated patient information.
     */
    public void updatePatient(Patient patient){
        bdAccess.getWritableDatabase().execSQL("UPDATE patient SET " +
                "name='"+patient.getName()+"', " +
                "firstName='"+patient.getFirstName()+"', " +
                "birthDate='"+patient.getBirthDate()+"', " +
                "remarks='" + patient.getRemarks() + "' " +
                "WHERE idPatient=" + patient.getId() + ";");
        bdAccess.close();
    }

    /**
     * Converts a database cursor to an {@code ArrayList<Patient>}.
     *
     * @param cursor The database cursor containing patient data.
     * @return An {@code ArrayList<Patient>} containing patient objects.
     */
    private ArrayList<Patient> cursorToPatientArrayList(Cursor cursor){
        ArrayList<Patient> listPatients = new ArrayList<>();
        long idPatient;
        String name;
        String firstName;
        String birthDate;
        String remarks;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            idPatient = cursor.getLong(0);
            name = cursor.getString(1);
            firstName = cursor.getString(2);
            birthDate = cursor.getString(3);
            remarks = cursor.getString(4);
            listPatients.add(new Patient(idPatient, name, firstName, birthDate, remarks));
            cursor.moveToNext();
        }

        return listPatients;
    }
}
