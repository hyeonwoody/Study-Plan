package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;

public class Background_settings_ extends AppCompatActivity {
    private Firebase mRef;
    ImageView clickMe,c2,c3,c4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_background);
        Firebase.setAndroidContext(this);
        mRef=new Firebase("https://app2-7ca9a.firebaseio.com/");

        Firebase mRefChild = mRef.child("background_setting_index");
        /*images = new int[] {R.drawable.bg ,R.drawable.olaf,R.drawable.flower,R.drawable.lala_land};

        View view = LayoutInflater.from(getApplication()).inflate(R.layout.activity_study_record_list, null);
        screenView =  view.findViewById(R.id.main1);
        //screenView.setVisibility(View.VISIBLE);
        screenView2 =  view.findViewById(R.id.main2);
        //screenView2.setVisibility(View.INVISIBLE);*/

        clickMe = (ImageView)findViewById(R.id.bt1);
        clickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefChild.setValue(1);
                Intent intent=new Intent(getApplicationContext(), TabActivity.class);
                //Window w=getLocalActivityManager().startActivity("1", i);
                //setContentView(w.getDecorView());
                startActivity(intent);
                finish();
            }
        });
        c2 = (ImageView)findViewById(R.id.bt2);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefChild.setValue(2);
                Intent i=new Intent(getApplicationContext(), TabActivity.class);
                //Window w=getLocalActivityManager().startActivity("1", i);
                //setContentView(w.getDecorView());
                startActivity(i);
                finish();
            }
        });
        c3 = (ImageView)findViewById(R.id.bt3);
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefChild.setValue(3);
                Intent i=new Intent(getApplicationContext(), TabActivity.class);
                //Window w=getLocalActivityManager().startActivity("1", i);
                //setContentView(w.getDecorView());
                startActivity(i);
                finish();
            }
        });
        c4 = (ImageView)findViewById(R.id.bt4);
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefChild.setValue(4);
                Intent i=new Intent(getApplicationContext(), TabActivity.class);
                //Window w=getLocalActivityManager().startActivity("1", i);
                //setContentView(w.getDecorView());
                startActivity(i);
                finish();
            }
        });
    }
}
