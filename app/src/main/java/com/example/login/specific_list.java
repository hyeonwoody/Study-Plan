package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class specific_list extends AppCompatActivity {

    private Firebase mRef;
    private TextView seetopic;
    private TextView book;
    private TextView subject;
    private TextView timeStart;
    private TextView fulltime;
    private TextView lefttime;
    private TextView contents;
    private TextView range;
    private TextView puni;
    int pressedTime=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_list);

        Intent i=getIntent();
        String f=i.getStringExtra("category");
        mRef=new Firebase("https://appproject-khi.firebaseio.com/");
        seetopic=(TextView)findViewById(R.id.topic);
        book=(TextView)findViewById(R.id.study_book);
        subject=(TextView)findViewById(R.id.study_subject);
        timeStart=(TextView)findViewById(R.id.study_start_timer);
        fulltime=(TextView)findViewById(R.id.study_fullseted_timer);
        lefttime=(TextView)findViewById(R.id.study_stopped_time);
        contents=(TextView)findViewById(R.id.study_contents);
        range=(TextView)findViewById(R.id.study_range);
        puni=(TextView)findViewById(R.id.punishment_check);

        contents.setText("공부한 내용 : ");
        puni.setText("중도 포기 시, 자신이 선택한 벌칙수행여부");

        Firebase a= mRef.child(f);

        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value=dataSnapshot.child("타이머_시작시간").getValue(String.class);
                seetopic.setText(value);
                String value2=dataSnapshot.child("공부한_책이름").getValue(String.class);
                book.setText("공부한 책 :  "+value2);
                //String value3=dataSnapshot.child("공부한_과목").getValue(String.class);
                //subject.setText(value3);
                String value4=dataSnapshot.child("현재시간").getValue(String.class);
                timeStart.setText("타이머 시작 시간 :  "+ value4);
                String value5=dataSnapshot.child("계획된_시간").getValue(String.class);
                fulltime.setText("계획된 시간 :  " + value5);

                String value6=dataSnapshot.child("공부한 시간").getValue(String.class);
                lefttime.setText("실제 공부한 시간 :  "+value6);
                String value8=dataSnapshot.child("공부범위").getValue(String.class);
                range.setText("공부범위(단원) :  "+value8);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        pressedTime++;
        if(pressedTime==0) {
            super.onBackPressed();
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}