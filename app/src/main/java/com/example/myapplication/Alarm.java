package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Alarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        Button stopWatch = (Button)findViewById(R.id.stopWatch);
        Button alarm = (Button)findViewById(R.id.alarm);
        Button timer = (Button)findViewById(R.id.timer);
        Button motion = (Button)findViewById(R.id.motion);

        stopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StopWatch.class);
                startActivity(intent);
                finish();
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Timer.class);
                startActivity(intent);
                finish();
            }
        });

        motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Motion.class);
                startActivity(intent);
                finish();
            }
        });
    }
}