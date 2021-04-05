package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class Timer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        //타이머 시간
        NumberPicker time = (NumberPicker)findViewById(R.id.time);
        time.setMinValue(0);
        time.setMaxValue(12);

        //타이머 분
        NumberPicker minute = (NumberPicker)findViewById(R.id.minute);
        minute.setMinValue(0);
        minute.setMaxValue(60);

        //타이머 초
        NumberPicker second = (NumberPicker)findViewById(R.id.second);
        second.setMinValue(0);
        second.setMaxValue(60);

        //버튼 선언
        Button stopWatch = (Button)findViewById(R.id.stopWatch);
        Button alarm = (Button)findViewById(R.id.alarm);
        Button timer = (Button)findViewById(R.id.timer);
        Button motion = (Button)findViewById(R.id.motion);

        //버튼 화면 이동
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


        motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Motion.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        //버튼 색깔, 글자 색깔
        timer.setBackgroundColor(Color.parseColor("#5AA3C3"));
        timer.setTextColor(Color.parseColor("#FFFFFF"));

    }
}