package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE timer (id INTEGER PRIMARY KEY" +
                " AUTOINCREMENT,use INTEGER,name TEXT, Vibration INTEGER," +
                "motion INTEGER, whatIsMotion INTEGER,time Long);");
        for (int i = 0; i < 5; i++){
            int use = 0;
            String name = "test" + i;
            int Vibration  = 0;
            int motion  = 0;
            int whatIsMotion  = 0;
            long time = 0;
            db.execSQL("INSERT INTO timer VALUES(null,'"+use+"','"+ name + "','" + Vibration + "','" + motion + "','" + whatIsMotion +
                    "','" +time+"')");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS timer");
        onCreate(db);
    }
}
