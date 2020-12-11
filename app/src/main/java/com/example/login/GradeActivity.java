package com.example.login;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Table;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button insert;
    private Button delete;
    private FloatingActionButton add_Score;

    private TableLayout tableLayout;
    private TableLayout tableLayout_2;

    private TableRow tableRow;
    private List<EditText> editTextList;
    private RelativeLayout relativeLayout;
    private ScrollView scrollView;
    private HorizontalScrollView horizontalScrollView;
    private FirebaseFirestore firestore;
    private CollectionReference reference;
    private CollectionReference score;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    final String student[] = new String[5];
    private TableRow[] table;

    private Firebase mRef;
    String value_2;
    String val_1;
    String val_2;
    String val_3;
    String val_4;
    String val_5;
    String val_6;

    private int pressTime = 0;
    private int count;
    String[][] info;
    ArrayList<ModelClass> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        mRef=new Firebase("https://app2-7ca9a.firebaseio.com/");
        Firebase as= mRef.child("email");
        as.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value_2=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase.setAndroidContext(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.context);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.list);

        add_Score = (FloatingActionButton) findViewById(R.id.addScore);
        add_Score.setOnClickListener(this);

        tableLayout = (TableLayout) findViewById(R.id.addInfo);
        tableLayout_2 = (TableLayout) findViewById(R.id.addInfo_2);
        addHeaders();
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Score").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) { // @Nullable
                try {
                    for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                        val_1 = documentChange.getDocument().getData().get("Grade").toString();
                        val_2 = documentChange.getDocument().getData().get("Rank").toString();
                        val_3 = documentChange.getDocument().getData().get("Score").toString();
                        val_4 = documentChange.getDocument().getData().get("Semester").toString();
                        val_5 = documentChange.getDocument().getData().get("Subject").toString();
                        val_6 = documentChange.getDocument().getData().get("Email").toString();
                        if(val_6.equals(value_2)) {
                            modelList = new ArrayList<>();
                            ModelClass modelClass = new ModelClass();
                            modelClass.setA_0(val_4);
                            modelClass.setA_1(val_5);
                            modelClass.setA_2(val_3);
                            modelClass.setA_3(val_2);
                            modelClass.setA_4(val_1);
                            modelList.add(modelClass);
                            addRows();
                        }
                    }
                }
                catch(NullPointerException ee) {
                }
            }
        });

    }

    public void addHeaders() {

        TableLayout tl = tableLayout_2;
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        //  tr.addView(getTextView(0, "Auditor id", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "학기", Color.BLACK, Typeface.BOLD, R.drawable.cell_shape ));
        tr.addView(getTextView(0, "과목", Color.BLACK, Typeface.BOLD, R.drawable.cell_shape ));
        tr.addView(getTextView(0, "성적", Color.BLACK, Typeface.BOLD, R.drawable.cell_shape ));
        tr.addView(getTextView(0, "등수", Color.BLACK, Typeface.BOLD, R.drawable.cell_shape ));
        tr.addView(getTextView(0, "등급", Color.BLACK, Typeface.BOLD, R.drawable.cell_shape ));

        tl.addView(tr, getTblLayoutParams());
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setBackgroundResource(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }


    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

    public void addRows(){
        // Collections.reverse(trainScheduleList);
        for (int i = 0; i < modelList.size(); i++) {
            TableRow tr = new TableRow(GradeActivity.this);
            tr.setLayoutParams(getLayoutParams());

            tr.addView(getRowsTextView(0, modelList.get(i).getA_0(), Color.BLACK, Typeface.BOLD, R.drawable.cell_shape ));
            tr.addView(getRowsTextView(0, modelList.get(i).getA_1(), Color.BLACK, Typeface.BOLD ,R.drawable.cell_shape ));
            tr.addView(getRowsTextView(0, modelList.get(i).getA_2(), Color.BLACK, Typeface.BOLD ,R.drawable.cell_shape ));
            tr.addView(getRowsTextView(0, modelList.get(i).getA_3(), Color.BLACK, Typeface.BOLD ,R.drawable.cell_shape ));
            tr.addView(getRowsTextView(0, modelList.get(i).getA_4(), Color.BLACK, Typeface.BOLD ,R.drawable.cell_shape ));

            tableLayout_2.addView(tr, getTblLayoutParams());
        }

    }

    private TextView getRowsTextView(int id, String title, int color, int typeface,int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundResource(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }

    @Override
    public void onClick(View view) {
        if (view == add_Score) {
            addTable();
        }

    }


    private void addTable() {
        count = tableLayout.getChildCount();
        tableRow = new TableRow(this);
        editTextList = new ArrayList<>();

        tableRow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        tableRow.setClickable(true);
        for (int i = 0; i < 5; i++) {
            EditText editText = new EditText(this);
            editTextList.add(editText);
            editText.setBackgroundResource(R.drawable.edit_text);
            editText.setGravity(Gravity.CENTER);
            editText.setLines(1);
            tableRow.addView(editText);
        }
        insert = new Button(this);
        insert.setText("저장");

        delete = new Button(this);
        delete.setText("삭제");

        tableRow.addView(insert);
        tableRow.addView(delete);

        final String[] Keys = new String[5];
        Keys[0] = "Semester";
        Keys[1] = "Subject";
        Keys[2] = "Score";
        Keys[3] = "Rank";
        Keys[4] = "Grade";

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, String> user = new HashMap<>();

                TableRow table = (TableRow) view.getParent();
                String mySemester = null;
                String mySubject = null;
                for (int i = 0; i < 5; i++) {
                    EditText ed = (EditText) table.getChildAt(i);
                    student[i] = ed.getText().toString().trim();
                    if (student[i].getBytes().length <= 0) {
                        Toast.makeText(GradeActivity.this, "값을 모두 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    switch (i % 5) {
                        case 0:
                            user.put(Keys[0], student[i]);
                            mySemester = student[i];
                            break;
                        case 1:
                            user.put(Keys[1], student[i]);
                            mySubject = student[i];
                            break;
                        case 2:
                            user.put(Keys[2], student[i]);
                            break;
                        case 3:
                            user.put(Keys[3], student[i]);
                            break;
                        case 4:
                            user.put(Keys[4], student[i]);
                            break;
                    }
                }
                reference = firestore.collection("User");
                firebaseUser = mAuth.getCurrentUser();
                checkData(user, mySemester, mySubject, Keys[0], Keys[1]);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Map<String, String> user = new HashMap<>();
                TableRow table = (TableRow) view.getParent();
                String mySemester = null;
                String mySubject = null;
                for (int i = 0; i < 5; i++) {
                    EditText ed = (EditText) table.getChildAt(i);
                    student[i] = ed.getText().toString().trim();
                    if (student[i].getBytes().length <= 0) {
                        tableLayout.removeView(table);
                        return;
                    }
                    switch (i) {
                        case 0:
                            user.put(Keys[0], student[i]);
                            mySemester = student[i];
                            break;
                        case 1:
                            user.put(Keys[1], student[i]);
                            mySubject = student[i];
                            break;
                        case 2:
                            user.put(Keys[2], student[i]);
                            break;
                        case 3:
                            user.put(Keys[3], student[i]);
                            break;
                        case 4:
                            user.put(Keys[4], student[i]);
                            break;
                    }
                }
                deleteData(user, mySemester, mySubject, Keys[0], Keys[1]);
                tableLayout.removeView(table);

            }
        });

        firebaseUser = mAuth.getCurrentUser();
        tableLayout.addView(tableRow);
        count = tableLayout.getChildCount();

    }

    private void deleteData(Map<String, String> user, final String mySemester, final String mySubject, final String semester, final String subject) {
        score = firestore.collection("Score");
        final String myEmail = firebaseUser.getEmail();

        score.document(myEmail + ", " + mySemester + ", " + mySubject).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    private void checkData(Map<String, String> user, final String mySemester, final String mySubject, final String semester, final String subject) {
        score = firestore.collection("Score");

        for (String str : user.keySet()) {
            String value = (String) user.get(str);
            if (value.length() <= 0) {
                return;
            }
        }

        Query query = score.whereEqualTo(semester, mySemester).whereEqualTo(subject, mySubject);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String rSemester = documentSnapshot.getString(semester);
                    String rSubject = documentSnapshot.getString(subject);
                    if (rSemester.equals(mySemester.trim()) && rSubject.equals(mySubject.trim())) {
                        Toast.makeText(GradeActivity.this, "이미 입력했습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                insertData(user, mySemester, mySubject);
            }
        });
    }

    private void insertData(final Map<String, String> user, final String mySemester, final String mySubject) {
        final String myEmail = firebaseUser.getEmail();
        Query query = reference.whereEqualTo("Email", myEmail);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String rEmail = documentSnapshot.getString("Email");
                    if (rEmail.equals(myEmail)) {
                        break;
                    }
                }
                user.put("Email", myEmail);
                firestore.collection("Score")
                        .document(myEmail + ", " + mySemester + ", " + mySubject)
                        .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
        insert.setEnabled(false);
    }

}
