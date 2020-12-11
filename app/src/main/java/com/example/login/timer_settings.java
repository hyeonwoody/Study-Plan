package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class timer_settings extends AppCompatActivity {

    private int a,b,c;
    private Firebase mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_settings);
        Firebase.setAndroidContext(this);
        mRef=new Firebase("https://appproject-khi.firebaseio.com/");

        ImageView aa2=(ImageView)findViewById(R.id.setting_icon_);
        aa2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Background_settings_.class);
                //Window w=getLocalActivityManager().startActivity("1", i);
                //setContentView(w.getDecorView());
                startActivity(i);
            }
        });

        final Integer[] hours=new Integer[]{0, 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
        final Integer[] minutes=new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31
                ,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59};
        final Integer[] seconds=new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31
                ,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59};
        final Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        final Spinner s2 = (Spinner) findViewById(R.id.spinner2);
        final Spinner s3 = (Spinner) findViewById(R.id.spinner3);
        //hours에 대한 spinner 관하여..
        ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(), R.layout.spin, hours);
        adapter.setDropDownViewResource(R.layout.spin_dropdown);
        s1.setAdapter(adapter);

        final TextView t = (TextView)findViewById(R.id.time_text);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                t.setText( hours[position] + "시간 " + s2.getSelectedItem().toString()+" 분"+ s3.getSelectedItem().toString()
                        +" 초");
                a=hours[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //minutes에 대한 spinner 관하여..
        ArrayAdapter adapter2=new ArrayAdapter(getApplicationContext(), R.layout.spin, minutes);
        adapter2.setDropDownViewResource(R.layout.spin_dropdown);
        s2.setAdapter(adapter2);

        //final TextView t2 = (TextView)findViewById(R.id.time_text);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                t.setText( s1.getSelectedItem().toString() + "시간 " + minutes[position]+" 분"+ s3.getSelectedItem().toString()
                        +" 초");
                b=minutes[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //seconds에 대한 spinner 관하여..
        ArrayAdapter adapter3=new ArrayAdapter(getApplicationContext(), R.layout.spin, seconds);
        adapter3.setDropDownViewResource(R.layout.spin_dropdown);
        s3.setAdapter(adapter3);

        //final TextView t3 = (TextView)findViewById(R.id.time_text);
        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                t.setText( s1.getSelectedItem().toString() + "시간 " + s2.getSelectedItem().toString()+" 분"+
                        seconds[position]+" 초");
                c=seconds[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });

        /*String first_val=s1.getSelectedItem().toString();
        String second_val=s2.getSelectedItem().toString();
        String third_val=s3.getSelectedItem().toString(); //계획된 시간*/

        //spinner_book_select에 관하여..
        final String[] books={"수능 국어", "수능 다큐 지구과학","기타"};
        final Spinner s4 = (Spinner) findViewById(R.id.spinner_book_select);
        ArrayAdapter adapter4=new ArrayAdapter(getApplicationContext(),R.layout.spin, books);
        adapter4.setDropDownViewResource(R.layout.spin_dropdown);
        s4.setAdapter(adapter4);
        
        String books_studied=s4.getSelectedItem().toString();       //공부한 책 이름

        //spinner_book_mission_select에 관하여..
        final String[] book_mission={"미션1", "미션2", "벌칙"};
        final Spinner s5 = (Spinner) findViewById(R.id.spinner_book_mission_select);
        ArrayAdapter adapter5=new ArrayAdapter(getApplicationContext(),R.layout.spin, book_mission);
        adapter5.setDropDownViewResource(R.layout.spin_dropdown);
        s5.setAdapter(adapter5);

        String range_or_mission= s5.getSelectedItem().toString();           //공부 범위

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String formatDate = sdfNow.format(date);

        Firebase mRefChild = mRef.child(formatDate).child("타이머_시작시간");
        mRefChild.setValue(formatDate);

        //data.setformatdate(formatDate);
        //+타이머 시작 시간(구체적 시간 단위로)..
        Date now_ = new Date();
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        String hr = String.valueOf(hour);
        String mn = String.valueOf(min);
        String sc = String.valueOf(sec);

        Firebase mRefChild3 = mRef.child(formatDate).child("현재시간");
        mRefChild3.setValue(hr + "시_" + min + "분_" + sec + "초");

        Firebase mRefChild4 = mRef.child(formatDate).child("공부한_책이름");
        mRefChild4.setValue(books_studied);

        Firebase mRefChild5 = mRef.child(formatDate).child("공부범위");
        mRefChild5.setValue(range_or_mission);


        //Intent.. 다음화면으로 값 전달하기..
        Button timer_start = (Button) findViewById(R.id.button_timer_start);
        timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), timer_operating_screen.class);
                intent.putExtra("시간", a);
                intent.putExtra("분", b);
                intent.putExtra("초", c);
                intent.putExtra("formatDate", formatDate);
                startActivity(intent);
            }
        });
    }
}