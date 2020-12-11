package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends ActivityGroup implements View.OnClickListener {

    private Button btn_logOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_record_list);

        btn_logOut = (Button)findViewById(R.id.logOut);
        mAuth = FirebaseAuth.getInstance();

        btn_logOut.setOnClickListener(this);

    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onClick(View view){
        if(view==btn_logOut){
            signOut();

            mAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }



}