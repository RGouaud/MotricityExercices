package com.example.ex_motricite;

import android.content.Context;
import android.database.Cursor;

public class TestDAO {
    private static final String BASE = "BDMotricity";
    private static final int VERSION = 1;
    private final BdSQLiteOpenHelper acesBD;
    public TestDAO(Context ct) {
        acesBD = new BdSQLiteOpenHelper(ct, BASE, null, VERSION);
    }

    public Test getTest(int idTest){
        Test test = null;
        try(Cursor cursor = acesBD.getReadableDatabase().rawQuery("select * from test where idTest="+idTest+";",null))
        {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                test = new Test(idTest, cursor.getString(1), cursor.getString(2));
            }
        }
        return test;
    }

 public void addTest(){
     acesBD.getWritableDatabase().execSQL("INSERT INTO test (path, date) VALUES ('" + operator.getName() + "','" + operator.getFirstName() + "');");
     acesBD.close();
 }


}
