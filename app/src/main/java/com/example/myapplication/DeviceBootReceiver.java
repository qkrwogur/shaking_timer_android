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

    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new DBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {

            // on device boot complete, reset the alarm
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//

            Cursor cursor = db.rawQuery("SELECT time FROM timer", null);



            if(cursor.moveToFirst()){
                do{

                    long millis = cursor.getLong(0);

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
                }while (cursor.moveToNext());

            }


        }
    }

}