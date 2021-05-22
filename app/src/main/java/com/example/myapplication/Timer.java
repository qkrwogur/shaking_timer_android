package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;


public class Timer extends AppCompatActivity {

    private Button startButton,stopButton,restButton;//시작 중지 취소 버튼
    private NumberPicker Num_time,Num_minute,Num_second;//시간 선택 시간,분,초
    private boolean timerRunning;//True 면 실행중
    private TextView mid_1,mid_2; //:중간 :
    private TextView time_print;//시간 출력

    private Long TimeLeft;
    private CountDownTimer CountDownTimer;
    private long EndTime;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        startButton = findViewById(R.id.timerStart);
        stopButton = findViewById(R.id.timerStop);
        restButton = findViewById(R.id.timerReset);
        Num_time = findViewById(R.id.time);
        Num_minute = findViewById(R.id.minute);
        Num_second = findViewById(R.id.second);
        mid_1 = findViewById(R.id.mid1);
        mid_2 = findViewById(R.id.mid2);
        time_print = findViewById(R.id.timePrint);



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

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeLeft=0L;
                TimeLeft=TimeLeft+(Num_time.getValue()*3600000);
                TimeLeft=TimeLeft+(Num_minute.getValue()*60000);
                TimeLeft=TimeLeft+(Num_second.getValue()*1000);


                startTimer();
            }
        });
        restButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });

    }
    public void startTimer(){
        EndTime = System.currentTimeMillis() + TimeLeft;

        timerRunning = true;
        CountDownTimer = new CountDownTimer(TimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeft = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                CountDownTimer.cancel();
                timerRunning = false;
                time_print.setText("00:00:00");

            }
        }.start();

        updateCountDownText();
        updateButtons();
    }
    public void pauseTimer(){
        CountDownTimer.cancel();
        if(timerRunning){
            stopButton.setText("계속");
            timerRunning = false;
        }else{
            stopButton.setText("일시정지");
            startTimer();
        }
    }
    public void resetTimer(){
        CountDownTimer.cancel();
        timerRunning = false;
        stopButton.setText("일시정지");
        updateButtons();
    }

    public void updateCountDownText(){
        int hour = (int)(TimeLeft/1000)/3600;
        int minutes = (int) ((TimeLeft / 1000) / 60)-(hour*60);
        int seconds = (int) (TimeLeft / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hour, minutes, seconds);
        time_print.setText(timeLeftFormatted);
    }

    public void updateButtons() {
        if(timerRunning){

            Num_time.setVisibility(View.INVISIBLE);
            Num_minute.setVisibility(View.INVISIBLE);
            Num_second.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.INVISIBLE);
            mid_1.setVisibility(View.INVISIBLE);
            mid_2.setVisibility(View.INVISIBLE);

            stopButton.setVisibility(View.VISIBLE);
            restButton.setVisibility(View.VISIBLE);
            time_print.setVisibility(View.VISIBLE);
            //시작 버튼 클릭
        }else{
            stopButton.setVisibility(View.INVISIBLE);
            restButton.setVisibility(View.INVISIBLE);
            time_print.setVisibility(View.INVISIBLE);

            Num_time.setVisibility(View.VISIBLE);
            Num_minute.setVisibility(View.VISIBLE);
            Num_second.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.VISIBLE);
            mid_1.setVisibility(View.VISIBLE);
            mid_2.setVisibility(View.VISIBLE);

            //정지버튼 클릭
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("TimeLeft", TimeLeft);
        outState.putBoolean("timerRunning", timerRunning);
        outState.putLong("EndTime", EndTime);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TimeLeft = savedInstanceState.getLong("TimeLeft");
        timerRunning = savedInstanceState.getBoolean("timerRunning");
        updateCountDownText();
        updateButtons();
        if (timerRunning) {
            EndTime = savedInstanceState.getLong("EndTime");
            TimeLeft = EndTime - System.currentTimeMillis();
            startTimer();
        }
    }
}