package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private Button confirm;
    private EditText enter_email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        confirm = (Button)findViewById(R.id.btn_sendEmail);
        enter_email = (EditText)findViewById(R.id.edit_confirmEmail);

        confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        mAuth = FirebaseAuth.getInstance();
        String email = enter_email.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            enter_email.setError("이메일을 입력하세요");
            enter_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            enter_email.setError("이메일 형식이 맞지 않습니다");
            enter_email.requestFocus();
            return;
        }

        //firebase에서 자체적으로 제공하는 메소드, 로그인한 계정으로 이메일을 보내서 비밀번호를 갱신하게 함
        //따로 비밀번호 입력하는 Activity를 만들 필요 없이 Toast 메시지만 띄우고 login 화면으로 넘어감
        mAuth.sendPasswordResetEmail(email) 
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"이메일을 보냈습니다",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"이메일을 보내는데 실패했습니다",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}