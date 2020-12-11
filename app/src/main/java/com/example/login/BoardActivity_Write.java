package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import android.view.Window;
import android.app.LocalActivityManager;

public class BoardActivity_Write extends AppCompatActivity implements View.OnClickListener{

    private FirebaseFirestore myStore= FirebaseFirestore.getInstance();
    private FirebaseFirestore myStore1= FirebaseFirestore.getInstance();
    private EditText myWriteTitleText;
    private EditText myWriteContentText;
    private EditText myWriteNameText;

    public static BoardActivity Board;

    private String id;

    private SimpleDateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        myWriteTitleText=findViewById(R.id.write_title_text);
        myWriteContentText=findViewById(R.id.write_content_text);
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        findViewById(R.id.write_upload_button).setOnClickListener(this);
    }



    public void onClick(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getEmail();
        Log.i("★write", "intent 값 : "+user.getEmail());
        if (user.getEmail()!=null) {
            myStore.collection("User")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult() != null) {
                                for (DocumentSnapshot snap : task.getResult()) {
                                    String dataemail = (String) snap.getData().get("Email");
                                    if (dataemail.equals(user.getEmail())) {
                                        String name = (String) snap.getData().get("NickName");

                                        Log.i("★", "USER EMAIL : "+user.getEmail()+"\n 별명 : "+name);
                                        id = myStore.collection("board").document().getId();
                                        Intent intent = getIntent();
                                        String currentBoard = intent.getStringExtra("currentBoard");
                                        intent.putExtra("currentBoard", currentBoard);
                                        if (currentBoard!=null) {
                                            Map<String, Object> post = new HashMap<>();
                                            post.put("id", id);
                                            post.put("title", myWriteTitleText.getText().toString());
                                            post.put("content", myWriteContentText.getText().toString());
                                            post.put("name", name);
                                            Date time = new Date();
                                            post.put("date", dateFormat.format(time));
                                            myStore1.collection("board")
                                                    .document("root")
                                                    .collection(currentBoard)
                                                    .document(myWriteTitleText.getText().toString())
                                                    .set(post)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Log.d("★", "글 게시 성공 ");
                                                            //Toast.makeText(BoardActivity_Write.this, "업로드 성공!", Toast.LENGTH_SHORT).show();
                                                            (BoardActivity.Board).back();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Log.d("★", "글 게시 실패 ");
                                                            //Toast.makeText(BoardActivity_Write.this, "실패", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                        }
                                        else {
                                            Log.d("★", "현재 게시판을 알 수 없어 글 게시를 못해요 ");
                                            (BoardActivity.Board).back();
                                        }
                                        break;
                                    }

                                }
                            }
                        }

                    });
        }
        
        else {
            Log.d("★", "이메일을 알 수 없어 글 게시를 못하는 상황 ");
        }

    }
}