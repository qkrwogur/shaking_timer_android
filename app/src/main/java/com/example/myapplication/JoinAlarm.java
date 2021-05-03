package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class JoinAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_alarm);

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

        Button cancel =(Button)findViewById(R.id.cancel_join);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}