package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

        final ArrayList alarmList = new ArrayList();
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));
        alarmList.add(new ListAlarm("10:00",true));
        alarmList.add(new ListAlarm("10:00",false));

        CustomAdapter adapter = new CustomAdapter(this,R.layout.alarm_list,alarmList);
        ListView listView =(ListView)findViewById(R.id.printList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {


                ListAlarm listAlarm = (ListAlarm)alarmList.get(pos);

                String a = String.valueOf(pos);
                Toast toast = Toast.makeText(getApplicationContext(),listAlarm.time,Toast.LENGTH_LONG);
                toast.show();
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
        ListAlarm(String time,boolean on){
            this.time=time;
            this.on = on;
        }
        String time;
        boolean on;
    }

    

}