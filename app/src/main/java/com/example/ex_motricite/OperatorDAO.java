package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class OperatorDAO {
    private static String base = "BDMotricity";
    private static int version = 1;
    private BdSQLiteOpenHelper accesBD;
    public OperatorDAO(Context ct) {
        accesBD = new BdSQLiteOpenHelper(ct, base, null, version);
    }

    public Operator getOperator(long idOperator){
        Operator anOperator = null;
        Cursor curseur;
        curseur = accesBD.getReadableDatabase().rawQuery("select * from operator where idOperator="+idOperator+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            anOperator = new Operator(idOperator, curseur.getString(1),curseur.getString(2));
        }
        return anOperator;
    }

    public void addOperator(Operator operator){
        accesBD.getWritableDatabase().execSQL("INSERT INTO operator (name, firstName) VALUES ('" + operator.getName() + "','" + operator.getFirstName() + "');");
        accesBD.close();
    }

    public void delOperator(Operator operator){
        accesBD.getWritableDatabase().execSQL("delete from operator where idOperator="+operator.getId()+";");
        accesBD.close();
    }

    public ArrayList<Operator> getOperators(){
        Cursor curseur;
        String req = "select * from operator;";
        curseur = accesBD.getReadableDatabase().rawQuery(req,null);
        return cursorToOperatorArrayList(curseur);
    }

    public void updateOperator(Operator operator) {
        accesBD.getWritableDatabase().execSQL("UPDATE operator SET " +
                "name='" + operator.getName() + "', " +
                "firstName='" + operator.getFirstName() + "' " +  // Correction : Suppression de la virgule ici
                "WHERE idOperator=" + operator.getId() + ";");
        accesBD.close();
    }

    private ArrayList<Operator> cursorToOperatorArrayList(Cursor curseur){
        ArrayList<Operator> listOperators = new ArrayList<Operator>();
        long idOperator;
        String name;
        String firstName;

        curseur.moveToFirst();
        while (!curseur.isAfterLast()){
            idOperator = curseur.getLong(0);
            name = curseur.getString(1);
            firstName = curseur.getString(2);
            listOperators.add(new Operator(idOperator, name, firstName));
            curseur.moveToNext();
        }

        return listOperators;
    }
}
