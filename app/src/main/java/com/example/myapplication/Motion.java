package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Motion extends AppCompatActivity {

    RadioButton ShakeRadio,TurnRadio;
    boolean whatIsMotion;

    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion);

        dbHelper = new DBHelper(this,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        Button stopWatch = (Button)findViewById(R.id.stopWatch);
        Button alarm = (Button)findViewById(R.id.alarm);
        Button timer = (Button)findViewById(R.id.timer);
        Button motion = (Button)findViewById(R.id.motion);
        ShakeRadio = findViewById(R.id.shakeRadio);
        TurnRadio = findViewById(R.id.turnRadio);

        stopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StopWatch.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alarm.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Timer.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        motion.setBackgroundColor(Color.parseColor("#5AA3C3"));
        motion.setTextColor(Color.parseColor("#FFFFFF"));


        Cursor cursor = db.rawQuery("SELECT whatIsMotion FROM timer WHERE id='"+1+"'", null);
        while(cursor.moveToNext()){
            whatIsMotion=(cursor.getInt(0)==0);
            System.out.println(whatIsMotion+" 여기야여기");
        }

        if(whatIsMotion){
            ShakeRadio.setChecked(true);
        }else{
            TurnRadio.setChecked(true);
        }


        TurnRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TurnRadio.isChecked()){
                    Toast.makeText(getApplicationContext(),"모션을 회전으로 설정", Toast.LENGTH_SHORT).show();
                    db.execSQL("UPDATE timer set whatIsMotion = '"+1+"'");
                }
            }
        });
       ShakeRadio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(ShakeRadio.isChecked()){
                   Toast.makeText(getApplicationContext(),"모션을 흔들기로 설정", Toast.LENGTH_SHORT).show();
                   db.execSQL("UPDATE timer set whatIsMotion = '"+0+"'");
               }
           }
       });


    }
}