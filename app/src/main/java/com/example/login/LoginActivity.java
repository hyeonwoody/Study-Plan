package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9091;
    private SignInButton signInButton;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Button btn_register;
    private Button btn_logIn;
    private Button btn_findPw;
    private EditText editEmail;
    private EditText editPassword;
    private ImageView imageContents;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    public SharedPreferences.Editor editor;
    private String email,password;

    private Firebase mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        mRef=new Firebase("https://app2-7ca9a.firebaseio.com/");

        imageContents = (ImageView)findViewById(R.id.image_contents);
        signInButton = (SignInButton)findViewById(R.id.signButton);
        btn_register = (Button)findViewById(R.id.Register);
        btn_logIn = (Button)findViewById(R.id.logIn);
        btn_findPw = (Button)findViewById(R.id.Password);
        editEmail = (EditText)findViewById(R.id.Email);
        editPassword = (EditText)findViewById(R.id.pass);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){ //이미 로그인 되어 있을 경우에 MainActivity 화면으로 넘어간다.
            finish();
            //Firebase mRefChild0 = mRef.child("email"); //added for profile at home_part
            //mRefChild0.setValue(email);              //added for profile at home_part
            startActivity(new Intent(LoginActivity.this, TabActivity.class));
        }

        Glide.with(this)
                .load("http://goo.gl/gEgYUd")
                .override(300,200)
                .fitCenter()
                .into(imageContents); //구글 이미지 가져오는 코드

        GoogleSignInOptions goo = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,goo); //구글 계정 요청하는 코드

        signInButton.setOnClickListener(new View.OnClickListener() { //구글 로그인 버튼 누를시 작동하는 코드
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btn_register.setOnClickListener(this);
        btn_logIn.setOnClickListener(this);
        btn_findPw.setOnClickListener(this);
    }

    private void login(){
        email = editEmail.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            editEmail.setError("이메일을 입력하세요");
            editEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            editPassword.setError("비밀번호를 입력하세요");
            editPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("이메일 형식이 맞지 않습니다");
            editEmail.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password) //이메일, 비밀번호로 로그인하는 구글 메소드.
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            Firebase mRefChild = mRef.child("email"); //added for profile at home_part
                            mRefChild.setValue(email);              //added for profile at home_part
                            startActivity(new Intent(LoginActivity.this, TabActivity.class));
                        } else{
                            Toast.makeText(LoginActivity.this,"이메일이나 비밀번호가 맞지 않습니다",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v){
        if(v==btn_register){ //회원가입 버튼
            Intent intent = new Intent(this, RegisActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v==btn_logIn){ //로그인 버튼
            login();
        }
        else if(v==btn_findPw){ //비밀번호 찾기 버튼
            Intent intent = new Intent(this, FindPasswordActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn(){ //구글 로그인 버튼 눌렀을때 작동 메소드
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }catch(ApiException e){

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){ //구글 계정 인증하는 메소드
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        final String join_date = simpleDateFormat.format(calendar.getTime());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        firestore = FirebaseFirestore.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser(); //구글 사용자 정보 받아오기
                            updateUI(user);//해당 계정으로 MainActivity 화면으로 이동
                            checkGoogleEmail(user,join_date,firestore); //구글 이메일 중복 점검
                        }else{
                            updateUI(null); //로그인 실패시 변화없음
                        }
                    }
                });
    }

    //구글 이메일 중복 점검, 이미 가입된 이메일이면 넘어가고 처음 가입한 이메일이면 User DB에 추가하는 메소드
    private void checkGoogleEmail(final FirebaseUser user, final String join_date, final FirebaseFirestore firestore){
        collectionReference = firestore.collection("User");
        final String myEmail = user.getEmail();

        Query query = collectionReference.whereEqualTo("Email",myEmail);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    String rEmail = documentSnapshot.getString("Email");
                    if(rEmail.equals(myEmail)){
                        return;
                    }
                }
                Map<String,String> users = new HashMap<>();
                users.put("Email",user.getEmail());
                users.put("NickName","Study"+user.getDisplayName());
                users.put("Join_Date",join_date);
                updateUI(user);//해당 계정으로 MainActivity 화면으로 이동
                firestore.collection("User").add(users) //사용자 정보 담는 User 데이터베이스에 저장
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                            }
                        });
            }
        });
    }
    private void updateUI(FirebaseUser user){
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(), TabActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

}


