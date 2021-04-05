package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Motion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion);

        Button stopWatch = (Button)findViewById(R.id.stopWatch);
        Button alarm = (Button)findViewById(R.id.alarm);
        Button timer = (Button)findViewById(R.id.timer);
        Button motion = (Button)findViewById(R.id.motion);

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


    }
}