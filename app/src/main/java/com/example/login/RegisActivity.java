package com.example.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class RegisActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail;
    private EditText editNickname;
    private EditText editPassword;
    private EditText editRePassword;
    private Button btn_okay;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private CollectionReference reference;
    private boolean nameExisting;

    public final static String TAG = "RegisActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        editNickname = (EditText)findViewById(R.id.btn_nickname); //nickname 입력칸
        editEmail = (EditText)findViewById(R.id.btn_email); //email 입력칸
        editPassword = (EditText)findViewById(R.id.btn_password); //비밀번호 입력칸
        editRePassword = (EditText)findViewById(R.id.btn_rePassword); //비밀번호 재입력칸
        btn_okay = (Button)findViewById(R.id.myId); //회원가입 완료버튼

        mAuth = FirebaseAuth.getInstance(); //FirebaseAuth 권한

        btn_okay.setOnClickListener(this);
    }

    private void register(){

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");// 현재 날짜 출력하기 위한 객체 생성
        String myNickname = editNickname.getText().toString().trim();
        String myEmail = editEmail.getText().toString().trim();
        String myPassword = editPassword.getText().toString().trim();
        String myRePassword = editRePassword.getText().toString().trim();
        String join_date = simpleDateFormat.format(calendar.getTime());

        if(TextUtils.isEmpty(myNickname)){
            editNickname.setError("Nickname을 입력하세요");
            editNickname.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(myEmail)){
            editEmail.setError("Email을 입력하세요");
            editEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(myPassword)){
            editPassword.setError("비밀번호를 입력하세요");
            editPassword.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(myRePassword)){
            editRePassword.setError("비밀번호를 한번 더 입력하세요");
            editRePassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(myEmail).matches()){
            editEmail.setError("이메일 형식이 맞지 않습니다");
            editEmail.requestFocus();
            return;
        }
        if(!myPassword.equals(myRePassword)){
            editRePassword.setError("비밀번호가 일치하지 않습니다");
            editRePassword.requestFocus();
            return;
        }

        checkNickName(myNickname,myPassword,myEmail,join_date);

    }

    //닉네임 중복 체크 메소드
    private void checkNickName(final String myNickname, final String myPassword, final String myEmail, final String join_date){
        db = FirebaseFirestore.getInstance();
        reference = db.collection("User");
        Query q1 = reference.whereEqualTo("NickName",myNickname);
        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                nameExisting = false;
                for(DocumentSnapshot ds:queryDocumentSnapshots){
                    String rNickName = ds.getString("NickName");
                    if(rNickName.equals(myNickname)){
                        editNickname.setError("이미 존재하는 nickname입니다.");
                        editNickname.requestFocus();
                        nameExisting=true;
                        break;
                    }
                }
                if(nameExisting==false){
                    createUserAccount(myNickname,myPassword,myEmail,join_date);
                }
            }
        });
    }

    //회원가입 메소드(Firebase Email,PW 인증 기능 이용)
    private void createUserAccount(final String myNickname, final String myPassword, final String myEmail, final String join_date){
        mAuth.createUserWithEmailAndPassword(myEmail,myPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegisActivity.this,"이미 사용중인 이메일이거나 비밀번호가 적합하지 않습니다",Toast.LENGTH_SHORT).show();
                        }else{
                            saveUserDB(myNickname,myPassword,myEmail,join_date);
                            Intent intent = new Intent(RegisActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    //회원가입 직후 사용자 정보를 "User" 데이터베이스에 저장하는 메소드
    private void saveUserDB(String myNickName,String myPassword,String myEmail,String join_date){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String,String> user = new HashMap<>();
        user.put("Email",myEmail);
        user.put("NickName",myNickName);
        user.put("Join_Date",join_date);

        firestore.collection("User").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v){
        if(v==btn_okay){
            register();
        }
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(RegisActivity.this,LoginActivity.class));
        finish();
    }
}