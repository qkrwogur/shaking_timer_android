package com.example.myapplication;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;



public class DeviceBootReceiver extends BroadcastReceiver {
    DBHelper dbHelper;
    SQLiteDatabase db;

    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;

    long millis;
    int id;
    boolean use;

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new DBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            //알람을 다시 저장
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);

            Cursor cursor = db.rawQuery("SELECT time,id,use FROM timer", null);

            if(cursor.moveToFirst()){
                do{

                    millis = cursor.getLong(0);
                    id = cursor.getInt(1);
                    use = (cursor.getInt(2)==0);
                    if(use){
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0);

                        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


                        Calendar current_calendar = Calendar.getInstance();
                        Calendar nextNotifyTime = new GregorianCalendar();
                        nextNotifyTime.setTimeInMillis(millis);

                        if (current_calendar.after(nextNotifyTime)) {
                            nextNotifyTime.add(Calendar.DATE, 1);
                        }

                        if (manager != null) {
                            manager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotifyTime.getTimeInMillis(),
                                    AlarmManager.INTERVAL_DAY, pendingIntent);
                        }

                        Date currentDateTime = nextNotifyTime.getTime();
                        String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                        Toast.makeText(context.getApplicationContext(),date_text + "으로 알람이 설정됨", Toast.LENGTH_SHORT).show();
                    }


                }while (cursor.moveToNext());

            }


        }
    }

}