package com.example.ex_motricite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DeletedTestDAO {
    private static final String BASE = "BDMotricity";
    private static final int VERSION = 1;
    private final BdSQLiteOpenHelper accesBd;
    public DeletedTestDAO(Context ct) {
        accesBd = new BdSQLiteOpenHelper(ct, BASE, null, VERSION);
    }

    public DeletedTest getTest(long idTest){
        DeletedTest test = null;
        Cursor cursor = accesBd.getReadableDatabase().rawQuery("select * from DeletedTest where idTest="+idTest+";",null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            test = new DeletedTest(idTest, cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        return test;
    }

    public long addTest(DeletedTest test){
        long idDeletedTest;
        ContentValues values = new ContentValues();
        values.put("path", test.getPath());
        values.put("suppressionDate", test.getSuppressionDate());
        idDeletedTest = accesBd.getWritableDatabase().insert("DeletedTest", null, values);
        accesBd.close();
        return idDeletedTest;
    }
    public void delTest(DeletedTest test){
        accesBd.getWritableDatabase().execSQL("delete from DeletedTest where idTest="+test.getId()+";");
        accesBd.close();
    }

    public ArrayList<DeletedTest> getAllTests(){
        Cursor cursor;
        String req = "select * from DeletedTest;";
        cursor = accesBd.getReadableDatabase().rawQuery(req,null);
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
