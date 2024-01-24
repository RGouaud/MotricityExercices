package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class PatientDAO {
    private static final String BASE = "BDMotricity";
    private static final int VERSION = 1;
    private final BdSQLiteOpenHelper bdAccess;
    public PatientDAO(Context ct) {
        bdAccess = new BdSQLiteOpenHelper(ct, BASE, null, VERSION);
    }

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

    public void addPatient(Patient patient){
        bdAccess.getWritableDatabase().execSQL("INSERT INTO patient (name, firstName, birthDate, remarks) VALUES ('" + patient.getName() + "','" + patient.getFirstName() + "','" + patient.getBirthDate() + "','" + patient.getRemarks() + "');");
        bdAccess.close();
    }

    public void delPatient(Patient patient){
        bdAccess.getWritableDatabase().execSQL("delete from patient where idPatient="+patient.getId()+";");
        bdAccess.close();
    }

    public ArrayList<Patient> getPatients(){
        Cursor cursor;
        String req = "select * from patient;";
        cursor = bdAccess.getReadableDatabase().rawQuery(req,null);
        return cursorToPatientArrayList(cursor);
    }

    public void updatePatient(Patient patient){
        bdAccess.getWritableDatabase().execSQL("UPDATE patient SET " +
                "name='"+patient.getName()+"', " +
                "firstName='"+patient.getFirstName()+"', " +
                "birthDate='"+patient.getBirthDate()+"', " +
                "remarks='" + patient.getRemarks() + "' " +
                "WHERE idPatient=" + patient.getId() + ";");
        bdAccess.close();
    }

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
