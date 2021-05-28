package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;


public class Timer extends AppCompatActivity  implements SensorEventListener {

    //타이머 변수들
    private Button startButton,stopButton,restButton,endButton;//시작 중지 취소 종료 버튼
    private NumberPicker Num_time,Num_minute,Num_second;//시간 선택 시간,분,초
    private boolean timerRunning;//True 면 실행중
    private TextView mid_1,mid_2; //:중간 :
    private TextView time_print;//시간 출력
    private Long TimeLeft;
    private CountDownTimer CountDownTimer;
    private long EndTime;
    private Vibrator vibrator;

    private Switch MotionSwitch;

    //흔들림 감지 변수들
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;

    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    private boolean end;

    private int moveCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//흔들기
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//흔들기

        startButton = findViewById(R.id.timerStart);
        stopButton = findViewById(R.id.timerStop);
        restButton = findViewById(R.id.timerReset);
        Num_time = findViewById(R.id.time);
        Num_minute = findViewById(R.id.minute);
        Num_second = findViewById(R.id.second);
        mid_1 = findViewById(R.id.mid1);
        mid_2 = findViewById(R.id.mid2);
        time_print = findViewById(R.id.timePrint);
        endButton = findViewById(R.id.timerEnd);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        MotionSwitch = findViewById(R.id.motionSwitch);//모셔 스위치



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

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endButton.setVisibility(View.INVISIBLE);
                vibrator.cancel();
                updateButtons();
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
                TimeLeft=TimeLeft+(Num_second.getValue()*1000)+1000;


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

        MotionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckState();
            }
        });


    }
    private void CheckState(){
        if(MotionSwitch.isChecked()) {
            if (accelerormeterSensor != null)
                sensorManager.registerListener(this, accelerormeterSensor,
                        SensorManager.SENSOR_DELAY_GAME);
        }
        else{
            if (sensorManager != null)
                sensorManager.unregisterListener(this);
        }
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
                endButton.setVisibility(View.VISIBLE);
                vibrator.vibrate(new long[]{500,1000,500,1000},0);
                end=false;

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
        int seconds = (int) ((TimeLeft / 1000) % 60);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hour, minutes, seconds);
        time_print.setText(timeLeftFormatted);
    }

    public void updateButtons() {
        if(timerRunning){
            end=true;
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
    }/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("timerRunning", timerRunning);
        outState.putLong("EndTime", EndTime);

        if(timerRunning){
            CountDownTimer.cancel();
            outState.putLong("TimeLeft", TimeLeft);
        }

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TimeLeft = savedInstanceState.getLong("TimeLeft");
        timerRunning = savedInstanceState.getBoolean("timerRunning");
        updateCountDownText();
        updateButtons();
        CheckState();
        if (timerRunning) {
            EndTime = savedInstanceState.getLong("EndTime");
            TimeLeft = EndTime - System.currentTimeMillis();
            startTimer();
        }
    }*/

    @Override//여기가 화면 회전
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(end){
            pauseTimer();
        }else{
            endButton.setVisibility(View.INVISIBLE);
            vibrator.cancel();
            updateButtons();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;


                if (speed > SHAKE_THRESHOLD+1000) {
                    // doSomething
                    moveCount++;
                    System.out.println(speed);
                    if(end && moveCount>3){
                        moveCount=0;
                        pauseTimer();
                    }else if(moveCount>3){
                        moveCount=0;
                        endButton.setVisibility(View.INVISIBLE);
                        vibrator.cancel();
                        updateButtons();
                    }

                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }

    }



}