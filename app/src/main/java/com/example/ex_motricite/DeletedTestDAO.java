package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DeletedTestDAO {
    private static final String BASE = "BDMotricity";
    private static final int VERSION = 1;
    private final BdSQLiteOpenHelper acesBD;
    public DeletedTestDAO(Context ct) {
        acesBD = new BdSQLiteOpenHelper(ct, BASE, null, VERSION);
    }

    public DeletedTest getTest(int idTest){
        DeletedTest test = null;
        Cursor cursor = acesBD.getReadableDatabase().rawQuery("select * from DeletedTest where idTest="+idTest+";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            test = new DeletedTest(idTest, cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        return test;
    }

    public void addTest(DeletedTest test){
        acesBD.getWritableDatabase().execSQL("INSERT INTO DeletedTest (path, suppressionDate) VALUES ('" + test.getPath() + "','" + test.getSuppressionDate() + "');");
        acesBD.close();
    }
    public void delTest(DeletedTest test){
        acesBD.getWritableDatabase().execSQL("delete from DeletedTest where idTest="+test.getId()+";");
        acesBD.close();
    }

    public ArrayList<DeletedTest> getAllTests(){
        Cursor cursor;
        String req = "select * from DeletedTest;";
        cursor = acesBD.getReadableDatabase().rawQuery(req,null);
        return cursorToTestArrayList(cursor);
    }
    private ArrayList<DeletedTest> cursorToTestArrayList(Cursor cursor){
        ArrayList<DeletedTest> listTests = new ArrayList<>();
        int idTest;
        String path;
        String dateSuppression;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            idTest = cursor.getInt(0);
            path = cursor.getString(1);
            dateSuppression = cursor.getString(2);
            listTests.add(new DeletedTest(idTest, path, dateSuppression));
            cursor.moveToNext();
        }

        return listTests;
    }

}
