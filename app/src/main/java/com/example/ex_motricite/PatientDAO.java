package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class PatientDAO {
    private static String base = "BDMotricity";
    private static int version = 1;
    private BdSQLiteOpenHelper accesBD;
    public PatientDAO(Context ct) {
        accesBD = new BdSQLiteOpenHelper(ct, base, null, version);
    }

    public Patient getPatient(int idPatient){
        Patient patient = null;
        Cursor curseur;
        curseur = accesBD.getReadableDatabase().rawQuery("select * from patient where idPatient="+idPatient+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            patient = new Patient(idPatient, curseur.getString(1),curseur.getString(2), curseur.getString(3),curseur.getString(4));
        }
        return patient;
    }

    public void addPatient(Patient patient){
        accesBD.getWritableDatabase().execSQL("INSERT INTO patient (name, firstName, birthDate, remarks) VALUES ('" + patient.getName() + "','" + patient.getFirstName() + "','" + patient.getBirthDate() + "','" + patient.getRemarks() + "');");
        accesBD.close();
    }

    public void delPatient(Patient patient){
        accesBD.getWritableDatabase().execSQL("delete from patient where idPatient="+patient.getId()+";");
        accesBD.close();
    }

    public ArrayList<Patient> getPatients(){
        Cursor curseur;
        String req = "select * from patient;";
        curseur = accesBD.getReadableDatabase().rawQuery(req,null);
        return cursorToPatientArrayList(curseur);
    }

    public void updatePatient(Patient patient) {
        StringBuilder updateQuery = new StringBuilder("UPDATE Patient SET ");

        if (!patient.getName().isEmpty()) {
            updateQuery.append("name = '").append(patient.getName()).append("', ");
        }
        if (!patient.getFirstName().isEmpty()) {
            updateQuery.append("firstName = '").append(patient.getFirstName()).append("',");
        }
        if (!patient.getBirthDate().isEmpty()) {
            updateQuery.append("birthDate = '").append(patient.getBirthDate()).append("',");
        }
        // Don't check if remarks are empty, we could want to delete it.
        updateQuery.append("remarks = '").append(patient.getRemarks()).append("',");


        // Delete final coma if the request is not empty
        if (updateQuery.charAt(updateQuery.length() - 1) == ' ') {
            updateQuery.setLength(updateQuery.length() - 2); // Supprimer la virgule et l'espace
            updateQuery.append(" ");
            updateQuery.append("WHERE idPatient = '").append(patient.getId()).append("';");
        }
        accesBD.getWritableDatabase().execSQL(updateQuery.toString());
        accesBD.close();
    }

    private ArrayList<Patient> cursorToPatientArrayList(Cursor curseur){
        ArrayList<Patient> listPatients = new ArrayList<Patient>();
        long idPatient;
        String name;
        String firstName;
        String birthDate;
        String remarks;

        curseur.moveToFirst();
        while (!curseur.isAfterLast()){
            idPatient = curseur.getLong(0);
            name = curseur.getString(1);
            firstName = curseur.getString(2);
            birthDate = curseur.getString(3);
            remarks = curseur.getString(4);
            listPatients.add(new Patient(idPatient, name, firstName, birthDate, remarks));
            curseur.moveToNext();
        }

        return listPatients;
    }
}
