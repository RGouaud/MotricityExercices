package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class TestDAO {
    private static final String BASE = "BDMotricity";
    private static final int VERSION = 1;
    private final BdSQLiteOpenHelper acesBD;
    public TestDAO(Context ct) {
        acesBD = new BdSQLiteOpenHelper(ct, BASE, null, VERSION);
    }

    public Test getTest(int idTest){
        Test test = null;
        Cursor cursor = acesBD.getReadableDatabase().rawQuery("select * from test where idTest="+idTest+";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            test = new Test(idTest, cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        return test;
    }

    public void addTest(Test test){
        acesBD.getWritableDatabase().execSQL("INSERT INTO test (path, suppressionDate) VALUES ('" + test.getPath() + "','" + test.getSuppressionDate() + "');");
        acesBD.close();
    }
    public void delTest(Test test){
        acesBD.getWritableDatabase().execSQL("delete from test where idTest="+test.getId()+";");
        acesBD.close();
    }

    public ArrayList<Test> getAllTests(){
        Cursor cursor;
        String req = "select * from test;";
        cursor = acesBD.getReadableDatabase().rawQuery(req,null);
        return cursorToTestArrayList(cursor);
    }
    private ArrayList<Test> cursorToTestArrayList(Cursor cursor){
        ArrayList<Test> listTests = new ArrayList<>();
        int idTest;
        String path;
        String dateSuppression;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            idTest = cursor.getInt(0);
            path = cursor.getString(1);
            dateSuppression = cursor.getString(2);
            listTests.add(new Test(idTest, path, dateSuppression));
            cursor.moveToNext();
        }

        return listTests;
    }

}
