package com.example.ex_motricite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * The {@code OperatorDAO} class represents a DAO (Data Access Object) for operators.
 *
 * <p>
 * This class is used to retrieve operator data from a database. It provides methods to retrieve, add, delete, and update
 * operator data in the database.
 * </p>
 *
 * <p>
 * Author: Ferreria
 * Version: 1.0
 * </p>
 */
public class OperatorDAO {
    /**
     * The name of the database.
     */
    private static final String BASE = "BDMotricity";
    /**
     * The version of the database.
     */
    private static final int VERSION = 1;
    /**
     * The database access object.
     */
    private final BdSQLiteOpenHelper accesBD;

    /**
     * Constructor for OperatorDAO class.
     *
     * @param ct Android application context.
     */
    public OperatorDAO(Context ct) {
        accesBD = new BdSQLiteOpenHelper(ct, BASE, null, VERSION);
    }

    /**
     * Returns an operator based on its ID.
     *
     * @param idOperator ID of the operator.
     * @return A {@code Operator} object representing the operator; {@code null} if not found.
     */
    @SuppressLint("Recycle")
    public Operator getOperator(long idOperator){
        Operator anOperator = null;
        Cursor curseur;
        curseur = accesBD.getReadableDatabase().rawQuery("select * from Operator where idOperator="+idOperator+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            anOperator = new Operator(idOperator, curseur.getString(1),curseur.getString(2));
        }
        return anOperator;
    }

    /**
     * Adds an operator to the database.
     *
     * @param operator Operator to be added.
     */
    public long addOperator(Operator operator){
        long idOperator;
        ContentValues values = new ContentValues();
        values.put("name", operator.getName());
        values.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("Operator", null, values);
        accesBD.close();
        return idOperator;
    }

    /**
     * Deletes an operator from the database.
     *
     * @param operator Operator to be deleted.
     */
    public void delOperator(Operator operator){
        accesBD.getWritableDatabase().execSQL("delete from Operator where idOperator="+operator.getId()+";");
        accesBD.close();
    }

    /**
     * Returns a list of all operators in the database.
     *
     * @return List of all operators in the database.
     */
    public ArrayList<Operator> getOperators(){
        Cursor curseur;
        String req = "select * from Operator;";
        curseur = accesBD.getReadableDatabase().rawQuery(req,null);
        return cursorToOperatorArrayList(curseur);
    }

    /**
     * Updates an operator in the database.
     *
     * @param operator Operator to be updated.
     */
    public void updateOperator(Operator operator) {
        accesBD.getWritableDatabase().execSQL("UPDATE Operator SET " +
                "name='" + operator.getName() + "', " +
                "firstName='" + operator.getFirstName() + "' " +  // Correction : Suppression de la virgule ici
                "WHERE idOperator=" + operator.getId() + ";");
        accesBD.close();
    }

    /**
     * Returns a list of operators based on a cursor.
     *
     * @param curseur Cursor containing operator data.
     * @return An {@code ArrayList<Operator>} containing operators based on the cursor.
     */
    private ArrayList<Operator> cursorToOperatorArrayList(Cursor curseur){
        ArrayList<Operator> listOperators = new ArrayList<>();
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