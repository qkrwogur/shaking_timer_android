package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class JoinAlarm extends AppCompatActivity {

    private int i;
    DBHelper dbHelper;
    SQLiteDatabase db;


    public static Context mContext;
    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;


    public TextView nameAlarm;
    public Switch VibrationSwitch;
    public Switch MotionSwitch;
    public Switch UseSwitch;
    String name;
    boolean vi,mo,us,whatIsMotion;
    int Ivi,Imo,Ius,IWhatISMotion;
    Long time;

    Button Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_alarm);
        final Intent intent = getIntent();

        i = intent.getIntExtra("i",0);

        dbHelper = new DBHelper(this,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
        System.out.println("아이디"+i);

        nameAlarm = findViewById(R.id.nameAlarmEdt);
        VibrationSwitch = findViewById(R.id.vibrationSwitch);
        MotionSwitch = findViewById(R.id.motionSwitch);
        UseSwitch = findViewById(R.id.useSwitch);

        Delete = findViewById(R.id.delete);
        final TimePicker ker=(TimePicker)findViewById(R.id.ker);
        ker.setIs24HourView(true);



        if(i!=0){
            Cursor cursor = db.rawQuery("SELECT name,Vibration,motion,time,use,whatIsMotion FROM timer WHERE id='"+ i + "'", null);

            while(cursor.moveToNext()){
                name = cursor.getString(0);
                vi = (cursor.getInt(1)==0);
                mo = (cursor.getInt(2)==0);
                time = cursor.getLong(3);
                us =(cursor.getInt(4)==0);
                whatIsMotion=(cursor.getInt(5)==0);
            }
            System.out.println("시간"+time);
            Calendar nextNotifyTime = new GregorianCalendar();
            nextNotifyTime.setTimeInMillis(time);
            Date currentTime = nextNotifyTime.getTime();
            SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
            SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());
            int pre_hour = Integer.parseInt(HourFormat.format(currentTime));
            int pre_minute = Integer.parseInt(MinuteFormat.format(currentTime));

            if (Build.VERSION.SDK_INT >= 23 ){
                ker.setHour(pre_hour);
                ker.setMinute(pre_minute);
            }
            else{
                ker.setCurrentHour(pre_hour);
                ker.setCurrentMinute(pre_minute);
            }
            if(vi){
                Ivi=0;
            }else{
                Ivi=1;
            }
            if(mo){
                Imo=0;
            }else{
                Imo=1;
            }
            if(us){
                Ius=0;
            }else{
                Ius=1;
            }
            if(whatIsMotion){
                IWhatISMotion=0;
            }else{
                IWhatISMotion=1;
            }
            System.out.println(Ivi+" "+Imo+" "+Ius);
            nameAlarm.setText(name);
            VibrationSwitch.setChecked(vi);
            MotionSwitch.setChecked(mo);
            UseSwitch.setChecked(us);
            cursor.close();

        }else{
            Ivi=1;
            Imo=1;
            Ius=1;
        }



        Button cancel =(Button)findViewById(R.id.cancel_join);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alarm.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });



        /*알람 부분*/

        MotionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Imo = 0;

                }else{
                    Imo = 1;
                }
            }
        });

        VibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Ivi=0;
                }else{
                    Ivi=1;
                }
            }
        });

        UseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Ius=0;
                }else{
                    Ius=1;
                }
            }
        });

        Button button = (Button) findViewById(R.id.save_join);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                int hour, hour_24, minute;
                String am_pm;
                String timeName;
                timeName = nameAlarm.getText().toString();
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour_24 = ker.getHour();
                    minute = ker.getMinute();
                }
                else{
                    hour_24 = ker.getCurrentHour();
                    minute = ker.getCurrentMinute();
                }
                if(hour_24 > 12) {
                    am_pm = "PM";
                    hour = hour_24 - 12;
                }
                else
                {
                    hour = hour_24;
                    am_pm="AM";
                }

                // 현재 지정된 시간으로 알람 시간 설정
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }

                if (timeName.getBytes().length <= 0){
                    timeName="알람";
                }



                if(i!=0){
                    db.execSQL("UPDATE timer set time = '"+ (long)calendar.getTimeInMillis() + "' WHERE id = '"+ i + "' ");
                    db.execSQL("UPDATE timer set name = '"+ timeName+ "' WHERE id = '"+ i + "' ");
                    db.execSQL("UPDATE timer set motion = '"+ Imo + "' WHERE id = '"+ i + "' ");
                    db.execSQL("UPDATE timer set Vibration = '"+ Ivi + "' WHERE id = '"+ i + "' ");
                    db.execSQL("UPDATE timer set use = '"+ Ius + "' WHERE id = '"+ i + "' ");
                }else{
                    db.execSQL("INSERT INTO timer VALUES(null,'"+Ius+"','"+ timeName + "','" + Ivi + "','" + Imo + "','" + IWhatISMotion +
                            "','" +(long)calendar.getTimeInMillis()+"')");
                    Cursor cursor = db.rawQuery("SELECT id FROM timer WHERE time='"+ (long)calendar.getTimeInMillis() + "'", null);

                    while(cursor.moveToNext()){
                      i=cursor.getInt(0);
                    }

                }

                if(Ius==0){
                    Date currentDateTime = calendar.getTime();
                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                    Toast.makeText(getApplicationContext(),date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
                    diaryNotification(calendar,false);
                }else{
                    Date currentDateTime = calendar.getTime();
                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                    Toast.makeText(getApplicationContext(),date_text + "으로 알람이 취소됨", Toast.LENGTH_SHORT).show();
                    diaryNotification(calendar,true);

                }

                Intent intent = new Intent(getApplicationContext(), Alarm.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

            }

        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i!=0){
                    int hour, hour_24, minute;
                    String am_pm;

                    if (Build.VERSION.SDK_INT >= 23 ){
                        hour_24 = ker.getHour();
                        minute = ker.getMinute();
                    }
                    else{
                        hour_24 = ker.getCurrentHour();
                        minute = ker.getCurrentMinute();
                    }
                    if(hour_24 > 12) {
                        am_pm = "PM";
                        hour = hour_24 - 12;
                    }
                    else
                    {
                        hour = hour_24;
                        am_pm="AM";
                    }

                    // 현재 지정된 시간으로 알람 시간 설정
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    if (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.DATE, 1);
                    }

                    db.execSQL("DELETE FROM timer WHERE id = '" + i + "';");
                    diaryNotification(calendar,true);
                    Date currentDateTime = calendar.getTime();
                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                    Toast.makeText(getApplicationContext(),date_text + "으로 알람이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), Alarm.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });



    }

    void diaryNotification(Calendar calendar,boolean why)
    {

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, alarmIntent, i);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        System.out.println("성공");
        if (alarmManager != null) {

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
        if(why) {// 알람 취소
            System.out.println("취소");
            alarmManager.cancel(pendingIntent);

        }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Alarm.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

}