package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Alarm extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;


    static final String DATABASE_NAME = "time.db";
    static final int DATABASE_VERSION = 2;

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
        dbHelper.onUpgrade(db,0,0);

        Cursor cursor = db.rawQuery("SELECT id,name,use FROM timer", null);



        final ArrayList alarmList = new ArrayList();

        if(cursor.moveToFirst()){
            do{
                alarmList.add(new ListAlarm(cursor.getInt(0),cursor.getString(1),cursor.getInt(2)==0));
            }while (cursor.moveToNext());
            cursor.close();
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
            ListAlarm list =  getItem(pos);
            TextView time = (TextView) convertView.findViewById(R.id.list_time);
            Switch on =(Switch)convertView.findViewById(R.id.list_switch);

            time.setText(list.time);
            on.setChecked(list.on);



            return convertView;
        }
    }

    class ListAlarm {
        ListAlarm(int i,String time,boolean on){
            this.i = i;
            this.time=time;
            this.on = on;
        }
        String time;
        boolean on;
        int i;
    }

    

}