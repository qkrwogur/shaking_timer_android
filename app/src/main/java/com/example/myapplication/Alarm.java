package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.Locale;


public class Alarm extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;

    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;

   // Intent alarmIntent = new Intent(this, AlarmReceiverOut.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);

        Button stopWatch = (Button) findViewById(R.id.stopWatch);
        Button alarm = (Button) findViewById(R.id.alarm);
        Button timer = (Button) findViewById(R.id.timer);
        Button motion = (Button) findViewById(R.id.motion);




        stopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StopWatch.class);
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

        ImageView join =(ImageView)findViewById(R.id.plus);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),JoinAlarm.class);
                startActivity(intent);
            }
        });


        alarm.setBackgroundColor(Color.parseColor("#5AA3C3"));
        alarm.setTextColor(Color.parseColor("#FFFFFF"));

        /*---------------------------------------------------------------------------------*/
        dbHelper = new DBHelper(this,DATABASE_NAME,null,DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(db,0,0);


        Cursor cursor = db.rawQuery("SELECT id,name,use,time FROM timer", null);



        final ArrayList alarmList = new ArrayList();

        if(cursor.moveToFirst()){
            do{
                alarmList.add(new ListAlarm(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)==0,cursor.getLong(3)));
            }while (cursor.moveToNext());
            //cursor.close();
        }



        /*
        alarmList.add(new ListAlarm("10:00",true));
        */
        CustomAdapter adapter = new CustomAdapter(this,R.layout.alarm_list,alarmList);
        ListView listView =(ListView)findViewById(R.id.printList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {


                ListAlarm listAlarm = (ListAlarm)alarmList.get(pos);

                //Toast toast = Toast.makeText(getApplicationContext(),listAlarm.time,Toast.LENGTH_LONG);
                //toast.show();
                Intent intent = new Intent(getApplicationContext(), JoinAlarm.class);
                intent.putExtra("i",listAlarm.i);
                startActivity(intent);
                finish();


            }
        });


    }
    class CustomAdapter extends ArrayAdapter<ListAlarm>{
        Activity context;
        ArrayList list;
        int layout;
        CustomAdapter(Activity context, int layout, ArrayList list){
            super(context, layout,list);
            this.context=context;
            this.list=list;
            this.layout = layout;
        }
        public int getCount(){
            return list.size();
        }
        public ListAlarm getItem(int pos){
            return (ListAlarm)list.get(pos);
        }
        public long getItemId(int pos){
            return pos;
        }

        public View getView(int pos, View convertView, ViewGroup parent){
            if(convertView == null){//스크롤이 넘어 갈때 재 사용 되는 view
                LayoutInflater inflater =context.getLayoutInflater();
                convertView = inflater.inflate(layout, null,true);
            }
            final ListAlarm list =  getItem(pos);
            TextView time = (TextView) convertView.findViewById(R.id.list_time);
            TextView use = (TextView) convertView.findViewById(R.id.list_use);

            Calendar nextNotifyTime = new GregorianCalendar();
            nextNotifyTime.setTimeInMillis(list.timeIS);
            Date currentTime = nextNotifyTime.getTime();
            SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
            SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());
            int pre_hour = Integer.parseInt(HourFormat.format(currentTime));
            int pre_minute = Integer.parseInt(MinuteFormat.format(currentTime));

            time.setText(list.name+"  "+pre_hour+"시"+pre_minute+"분");
            if(list.on){
                System.out.println(list.on);
                use.setText("사용중");
            }else{
                System.out.println(list.on);
                use.setText("취소됨");
            }


            return convertView;
        }

    }

    class ListAlarm {
        ListAlarm(int i,String name,boolean on,Long timeIS){
            this.i = i;
            this.name=name;
            this.on = on;
            this.timeIS = timeIS;
        }
        String name;
        boolean on;
        int i;
        Long timeIS;
    }


}
