package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class JoinAlarm extends AppCompatActivity {

    private int i;
    DBHelper dbHelper;
    SQLiteDatabase db;

    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_alarm);
        Intent intent = getIntent();

        i = intent.getIntExtra("i",0);

        dbHelper = new DBHelper(this,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        if(i!=0){
            Cursor cursor = db.rawQuery("SELECT * FROM timer WHERE id = '"+ i + "';", null);

        }

        Button cancel =(Button)findViewById(R.id.cancel_join);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    class ListAlarm {
        ListAlarm(int i,String time,boolean on){
            this.i = i;
            this.time=time;
            this.on = on;
        }
        String time;
        boolean on;
        int i;
    }

}