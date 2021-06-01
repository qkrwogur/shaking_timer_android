package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class AlarmCall extends AppCompatActivity implements SensorEventListener {

    boolean slop;
    MediaPlayer mp = null;
    String time;
    String nowTime,nowMinute;
    int IntNowTime,IntNowMinute;
    private AudioManager mAudioManager;

    Long howTime;

    TextView timeText;
    Button cancel;

    DBHelper dbHelper;
    SQLiteDatabase db;

    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;

    boolean Vibration,motion,whatIsMotion;
    String name;

    boolean realV,realM,realW;
    String realN;

    private Vibrator vibrator;

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
        setContentView(R.layout.alarm_call);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        timeText = findViewById(R.id.alarmTimeCall);
        cancel = findViewById(R.id.alarmCancel);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//흔들기
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//흔들기

        dbHelper = new DBHelper(this,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int)(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*0.75),
                AudioManager.FLAG_SHOW_UI);

        mp = MediaPlayer.create(this, R.raw.qqqqq);
        mp.setVolume(1,1);
        mp.start();
        mp.setLooping(true);
        final Intent intent = getIntent();
        time = intent.getStringExtra("time");

        nowTime = time.substring(11,13);
        nowMinute = time.substring(14,16);
        IntNowTime = Integer.parseInt(nowTime);
        IntNowMinute = Integer.parseInt(nowMinute);



        Cursor cursor = db.rawQuery("SELECT time,Vibration,motion,whatIsMotion,name FROM timer", null);



        final ArrayList alarmList = new ArrayList();

        if(cursor.moveToFirst()){
            do{
                howTime=cursor.getLong(0);
                Vibration=(cursor.getInt(1)==0);
                motion = (cursor.getInt(2)==0);
                whatIsMotion = (cursor.getInt(3)==0);
                name = cursor.getString(4);

                Calendar nextNotifyTime = new GregorianCalendar();
                nextNotifyTime.setTimeInMillis(howTime);
                Date currentTime = nextNotifyTime.getTime();
                SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
                SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());
                int pre_hour = Integer.parseInt(HourFormat.format(currentTime));
                int pre_minute = Integer.parseInt(MinuteFormat.format(currentTime));

                if(pre_hour==IntNowTime && pre_minute==IntNowMinute){
                    realV=Vibration;
                    realM=motion;
                    realN=name;
                    realW=whatIsMotion;
                }

            }while (cursor.moveToNext());

        }

        timeText.setText(realN+" "+nowTime+":"+nowMinute+" 알람");



        if(realV){
            vibrator.vibrate(new long[]{500,1000,500,1000},0);
        }

        if(realM){
            if(realW){
                if (accelerormeterSensor != null){
                    sensorManager.registerListener(this, accelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);
                }else{
                    if(sensorManager != null){
                        sensorManager.unregisterListener(this);
                    }
                }
            }else{
                slop=true;
            }
        }



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                vibrator.cancel();
                finish();
            }
        });

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

                    if(moveCount>3){
                        moveCount=0;
                        mp.stop();
                        vibrator.cancel();
                        finish();
                    }

                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override//여기가 화면 회전
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(slop){
            mp.stop();
            vibrator.cancel();
            finish();
        }
    }
}