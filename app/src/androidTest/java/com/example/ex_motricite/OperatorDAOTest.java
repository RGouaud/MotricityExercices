package com.example.ex_motricite;

import static org.junit.Assert.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class OperatorDAOTest {
    /**
     * Instance of the OperatorDAO to be tested
     */
    private OperatorDAO operatorDAO;

    /**
     * The Operator.
     */
    private Operator operator;

    /**
     * The Retrieved operator.
     */
    private  Operator retrievedOperator;

    /**
     * The Id operator.
     */
    private long idOperator;

    /**
     * Instance to access the database
     */
    private BdSQLiteOpenHelper accesBD;

    /**
     * The context of the application
     */
    private Context ct;

    /**
     * The List operators.
     */
    private ArrayList<Operator> listOperators;

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
        operatorDAO = new OperatorDAO(ct);
        operator = new Operator( "name", "firstName");
        retrievedOperator = null;
        idOperator = 0;
        accesBD = new BdSQLiteOpenHelper(ct, "BDMotricity", null, 1);
        listOperators = new ArrayList<>();
    }

    /**
     * Close the database and set all the variables to null
     */
    @After
    public void tearDown(){
        accesBD.close();
        operatorDAO = null;
        operator = null;
        retrievedOperator = null;
        idOperator = 0;
        listOperators = null;
        accesBD = null;
    }

    /**
     * Test get operator where id is registered.
     */
    @Test
    public void testGetOperatorWhereIdIsRegistered() {
        // GIVEN
        ContentValues values = new ContentValues();
        values.put("name", operator.getName());
        values.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("operator", null, values);

        // WHEN
        retrievedOperator = operatorDAO.getOperator(idOperator);

        // THEN
        assertEquals("Operator name does not match", operator.getName(), retrievedOperator.getName());
        assertEquals("Operator first name does not match", operator.getFirstName(), retrievedOperator.getFirstName());
        accesBD.getWritableDatabase().execSQL("delete from operator where idOperator="+idOperator+";");
    }

    /**
     * Test get operator where id is not registered.
     */
    @Test
    public void testGetOperatorWhereIdIsNotRegistered() {
        // GIVEN
        idOperator = 0;

        // WHEN
        retrievedOperator = operatorDAO.getOperator(idOperator);

        // THEN
        assertNull("Operator is not null", retrievedOperator);
    }


    /**
     * Test add operator.
     */
    @Test
    public void TestAddOperator() {
        //GIVEN

        //WHEN
        idOperator = operatorDAO.addOperator(operator);

        //THEN
        curseur = accesBD.getReadableDatabase().rawQuery("select * from operator where idOperator="+idOperator+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            retrievedOperator = new Operator(idOperator, curseur.getString(1),curseur.getString(2));
        }
        assertEquals("Operator name does not match", operator.getName(), retrievedOperator.getName());
        assertEquals("Operator first name does not match", operator.getFirstName(), retrievedOperator.getFirstName());
        accesBD.getReadableDatabase().execSQL("delete from operator where idOperator="+idOperator+";");
    }

    /**
     * Test delete operator.
     */
    @Test
    public void TestDelOperator() {
        //GIVEN
        ContentValues values = new ContentValues();
        values.put("name", operator.getName());
        values.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("operator", null, values);

        curseur = accesBD.getReadableDatabase().rawQuery("select * from operator where idOperator="+idOperator+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            retrievedOperator = new Operator(idOperator, curseur.getString(1),curseur.getString(2));
        }

        //WHEN
        operatorDAO.delOperator(retrievedOperator);

        //THEN
        curseur = accesBD.getReadableDatabase().rawQuery("select * from operator where idOperator="+idOperator+";",null);
        try {
            assertNotNull(curseur);
            assertEquals("Operator not deleted", 0, curseur.getCount());

            //if the operator is not deleted, we delete it
            if (curseur.getCount() > 0) {
                accesBD.getReadableDatabase().execSQL("delete from operator where idOperator=" + idOperator + ";");
            }
        }finally {
            curseur.close();
        }
    }

    /**
     * Test get operators.
     */
    @Test
    public void TestGetOperators() {
        //GIVEN

        //WHEN
        listOperators = operatorDAO.getOperators();

        //THEN
        // select all operators in SQL query
        curseur = accesBD.getReadableDatabase().rawQuery("select * from operator;",null);
        // Using getOperators method to get all operators
        listOperators = operatorDAO.getOperators();
        // compare the size of the sql query and the size of the list, they must be equal, then we consider that it is good
        assertEquals("List size does not match", curseur.getCount(), listOperators.size());
        // close the cursor
        curseur.close();
    }

    /**
     * Test update operator.
     */
    @Test
    public void TestUpdateOperator() {
        //GIVEN
        ContentValues values = new ContentValues();
        values.put("name", operator.getName());
        values.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("operator", null, values);

        //WHEN
        retrievedOperator = operatorDAO.getOperator(idOperator);
        retrievedOperator.setName("newName");
        retrievedOperator.setFirstName("newFirstName");
        operatorDAO.updateOperator(retrievedOperator);

        //THEN
        curseur = accesBD.getReadableDatabase().rawQuery("select * from operator where idOperator="+idOperator+";",null);
        if (curseur.getCount() > 0) {
            curseur.moveToFirst();
            retrievedOperator = new Operator(idOperator, curseur.getString(1),curseur.getString(2));
        }
        assertEquals("Operator name does not match", "newName", retrievedOperator.getName());
        assertEquals("Operator first name does not match", "newFirstName", retrievedOperator.getFirstName());
        accesBD.getReadableDatabase().execSQL("delete from operator where idOperator="+idOperator+";");

    }

}