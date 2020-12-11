package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BoardActivity_Read extends AppCompatActivity implements View.OnClickListener{

    private TextView content;
    private TextView date;
    private TextView name;
    private TextView title;
    int pressedTime=-1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_read);

        content=findViewById(R.id.content);
        date=findViewById(R.id.date);
        name=findViewById(R.id.name);
        title=findViewById(R.id.title);

        Intent intent=getIntent();
        title.setText(intent.getStringExtra("boardtitle"));

        db.collection("board")
                .document("root")
                .collection(intent.getStringExtra("currentBoard"))
                .document(intent.getStringExtra("boardtitle"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snap = task.getResult();
                        title.setText((String)snap.getData().get("title"));
                        content.setText((String)snap.getData().get("content"));
                        date.setText((String)snap.getData().get("date"));
                        name.setText((String)snap.getData().get("name"));

                        Log.d("★", "게시글 불러오기 성공 ");
                    }
                });
    }
    @Override
    public void onClick(View v) {

    }
//    @Override
//    public void onBackPressed() {
//        pressedTime++;
//        if(pressedTime==0) {
//            super.onBackPressed();
//            Intent returnIntent = new Intent();
//            setResult(RESULT_OK, returnIntent);
//            finish();
//        }
//    }
}
