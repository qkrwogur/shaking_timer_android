package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;



public class StopWatch extends AppCompatActivity implements SensorEventListener {

    DBHelper dbHelper;
    SQLiteDatabase db;

    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;

    boolean whatIsMotion;
    boolean motion;

    private Button StopWatchStart,record,pause,stop;//타이머 버튼
    private TextView StopWatchText,RecordView;

    private Thread timeThread = null;
    private Boolean isRunning = false;
    private Switch StopWatchSwitch;

    private int moveCount;

    //흔들림 감지
    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_watch);

        dbHelper = new DBHelper(this,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        Button stopWatch = (Button)findViewById(R.id.stopWatch);
        Button alarm = (Button)findViewById(R.id.alarm);
        Button timer = (Button)findViewById(R.id.timer);
        Button motion = (Button)findViewById(R.id.motion);

        StopWatchSwitch = findViewById(R.id.stopWatchSwitch);


        StopWatchStart = findViewById(R.id.stopWatchStart);
        record = findViewById(R.id.btn_record);
        pause = findViewById(R.id.btn_pause);
        stop = findViewById(R.id.btn_stop);

        StopWatchText = findViewById(R.id.stopWatchText);
        RecordView = findViewById(R.id.recordView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//흔들기
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//흔들기

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

        motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Motion.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        stopWatch.setBackgroundColor(Color.parseColor("#5AA3C3"));
        stopWatch.setTextColor(Color.parseColor("#FFFFFF"));

        /*------------------------------------------------------------------*/
        StopWatchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning=true;

                v.setVisibility(View.GONE);
                record.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);

                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                record.setVisibility(View.GONE);
                StopWatchStart.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                RecordView.setText("");
                StopWatchText.setText("00:00:00:00");
                timeThread.interrupt();
                isRunning = !isRunning;
                pause.setText("일시정지");

            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordView.setText(RecordView.getText() + StopWatchText.getText().toString() + "\n");
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning) {
                    pause.setText("일시정지");
                } else {
                    pause.setText("시작");
                }
            }
        });

        StopWatchSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckState();
            }
        });

        Cursor cursor = db.rawQuery("SELECT whatIsMotion FROM timer WHERE id='"+ 1 + "'", null);

        while(cursor.moveToNext()){
            whatIsMotion=(cursor.getInt(0)==0);
        }

    }

    private void CheckState(){
        if(StopWatchSwitch.isChecked()) {
            if (accelerormeterSensor != null)
                sensorManager.registerListener(this, accelerormeterSensor,
                        SensorManager.SENSOR_DELAY_GAME);
            motion=true;
        }
        else{
            if (sensorManager != null)
                sensorManager.unregisterListener(this);
            motion=false;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);

            StopWatchText.setText(result);
        }
    };
    public class timeThread implements Runnable {
        @Override
        public void run() {
           int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;

                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                StopWatchText.setText("");
                                StopWatchText.setText("00:00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!whatIsMotion && motion){
            if(isRunning){
                RecordView.setText(RecordView.getText() + StopWatchText.getText().toString() + "\n");
            }
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


                if(whatIsMotion){
                    if (speed > SHAKE_THRESHOLD+1000) {
                        // doSomething
                        moveCount++;
                        System.out.println(speed);
                        if(isRunning && moveCount>3){
                            moveCount=0;
                            RecordView.setText(RecordView.getText() + StopWatchText.getText().toString() + "\n");
                        }

                    }
                }


                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }

    }

}