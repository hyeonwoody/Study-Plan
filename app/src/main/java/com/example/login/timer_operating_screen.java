package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;

public class timer_operating_screen extends AppCompatActivity{

    private TextView mTextViewCountDown;
    private TextView mTextViewPause;
    private TextView mTextViewAlert;
    int count;
    int store_count;
    boolean loopFlag = true;
    boolean isFirst = true;
    boolean isRun;
    int num;
    private Firebase mRef;
    MyThread thread;
    String f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_operating_screen);

        mRef=new Firebase("https://appproject-khi.firebaseio.com/");
        Intent second=getIntent();
        int a1=second.getIntExtra("시간",0);
        int a2=second.getIntExtra("분",0);
        int a3=second.getIntExtra("초",0);
        f=second.getStringExtra("formatDate");
        num=a1*3600+a2*60+a3;
        Firebase mRefChild=mRef.child(f).child("계획된_시간");
        if(num == 0) {
            mRefChild.setValue("#");
        }
        else {
            mRefChild.setValue(String.valueOf(a1) + "시간_" + String.valueOf(a2) + "분_" + String.valueOf(a3) + "초");
        }

        mTextViewCountDown = findViewById(R.id.operating_start_time);
        mTextViewPause=findViewById(R.id.give_up);
        mTextViewAlert=findViewById(R.id.alert_call);

        thread = new MyThread();
        if(isFirst) {
            isFirst=false;
            isRun=true;
            thread.start();
        }
        else {
            isRun=true;
        }
        mTextViewAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), AlertCall.class);
                startActivity(intent);
            }
        });
        //포기 클릭시 다음화면으로..
        mTextViewPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRun=false;
                store_count=num-count; //실제 공부한 시간..
                int b1=(store_count/60)/60;
                int b2=(store_count/60)%60;
                int b3=store_count%60;
                Firebase mRefChild00=mRef.child(f).child("공부량");
                mRefChild00.setValue(String.valueOf(store_count));
                Firebase mRefChild0=mRef.child(f).child("공부한 시간");
                mRefChild0.setValue(String.valueOf(b1)+"시간_"+String.valueOf(b2)+"분_"+String.valueOf(b3)+"초");
                popup_mission_complete p=popup_mission_complete.getInstance();
                p.show(getSupportFragmentManager(),popup_mission_complete.TAG);
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                mTextViewCountDown.setText(String.valueOf(msg.arg1/3600)+"시간 "+String.valueOf(msg.arg1%3600/60)+"분 "+
                        String.valueOf(msg.arg1%3600%60)+"초");
            }else if(msg.what==2){
                mTextViewCountDown.setText((String)msg.obj);
            }
        }
    };
    class MyThread extends Thread {
        @Override
        public void run() {
            try {
                count=num;
                while(loopFlag){
                    Thread.sleep(1000);
                    if(isRun){
                        count--;
                        Message message=new Message();
                        message.what=1;
                        message.arg1=count;
                        handler.sendMessage(message);
                        if(count==0){
                            message=new Message();
                            message.what=2;
                            message.obj="Finish!!";
                            handler.sendMessage(message);

                            Firebase mRefChild11=mRef.child(f).child("공부량");
                            mRefChild11.setValue(String.valueOf(num));
                            Firebase mRefChild1=mRef.child(f).child("공부한 시간");
                            int c1=(num/60)/60;
                            int c2=(num/60)%60;
                            int c3=num%60;
                            mRefChild1.setValue(String.valueOf(c1)+"시간_"+String.valueOf(c2)+"분_"+String.valueOf(c3)+"초");
                            loopFlag=false;
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    }
}