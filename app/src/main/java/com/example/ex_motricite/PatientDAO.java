package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class PatientDAO {
    private static String base = "BDMotricity";
    private static int version = 1;
    private BdSQLiteOpenHelper accesBD;
    public PatientDAO(Context ct) {
        accesBD = new BdSQLiteOpenHelper(ct, base, null, version);
    }

    public Patient getPatient(int idPatient){
        Patient lePatient = null;
        Cursor curseur;
        curseur = accesBD.getReadableDatabase().rawQuery("select * from patient where idPatient="+idPatient+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            lePatient = new Patient(idPatient, curseur.getString(1),curseur.getString(2), curseur.getString(3),curseur.getString(4));
        }
        return lePatient;
    }

    public void addPatient(Patient patient){
        accesBD.getWritableDatabase().execSQL("INSERT INTO patient (name, firstName, birthDate, remarks) VALUES ('" + patient.getName() + "','" + patient.getFirstName() + "','" + patient.getBirthDate() + "','" + patient.getRemarks() + "');");
        accesBD.close();
    }

    public void delPatient(Patient patient){
        accesBD.getWritableDatabase().execSQL("delete from plat where idP="+patient.getIdPatient()+";");
        accesBD.close();
    }

    public ArrayList<Patient> getPlats(){
        Cursor curseur;
        String req = "select * from patient;";
        curseur = accesBD.getReadableDatabase().rawQuery(req,null);
        return cursorToPatientArrayList(curseur);
    }

    public void updatePatient(String oldName, String newName, String newFirstName, String newBirthDate, String newRemarks) {
        StringBuilder updateQuery = new StringBuilder("UPDATE Patient SET ");

        if (!newName.isEmpty()) {
            updateQuery.append("name = '").append(newName).append("', ");
        }
        if (!newFirstName.isEmpty()) {
            updateQuery.append("firstName = '").append(newFirstName).append("', ");
        }
        if (!newBirthDate.isEmpty()) {
            updateQuery.append("birthDate = '").append(newBirthDate).append("', ");
        }
        if (!newRemarks.isEmpty()) {
            updateQuery.append("remarks = '").append(newRemarks).append("', ");
        }

        // Supprimer la virgule finale si la requête n'est pas vide
        if (updateQuery.charAt(updateQuery.length() - 1) == ' ') {
            updateQuery.setLength(updateQuery.length() - 2); // Supprimer la virgule et l'espace
            updateQuery.append(" ");
            updateQuery.append("WHERE name = '").append(oldName).append("';");

            accesBD.getWritableDatabase().execSQL(updateQuery.toString());
            accesBD.close();
        }
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
