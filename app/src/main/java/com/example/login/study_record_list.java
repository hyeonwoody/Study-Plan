package com.example.login;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class study_record_list extends ActivityGroup implements OnItemClickListener, View.OnClickListener{
//ActivityGroup
    private Button btn_logOut;
    private FirebaseAuth mAuth;

    private Firebase mRef;
    private ArrayList<String> ids=new ArrayList<>();
    private ListView mListView;
    String category;
    Integer num;
    String value;
    private Firebase mRef_2;
    String value_2;

    String account_email;
    String account_nick;
    String account_join;
    FirebaseFirestore firestore;

    int i=0;
    TextView w;
    TextView ww;
    TextView www;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_record_list);
        Firebase.setAndroidContext(this);

        mRef_2=new Firebase("https://app2-7ca9a.firebaseio.com/");
        Firebase as= mRef_2.child("email");

        as.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value_2=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                }

                try {
                    for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                        account_email = documentChange.getDocument().getData().get("Email").toString();
                        account_nick = documentChange.getDocument().getData().get("NickName").toString();
                        account_join = documentChange.getDocument().getData().get("Join_Date").toString();
                        if (account_email.equals(value_2)) {
                            w = (TextView) findViewById(R.id.account_email_address);
                            ww = (TextView) findViewById(R.id.nickname);
                            www = (TextView) findViewById(R.id.join_dates);
                            w.setText(account_email);
                            ww.setText(account_nick);
                            www.setText(account_join);
                            break;
                        }
                    }
                }
                catch(NullPointerException ee) {
                }
            }
        });

        ImageView mn1=(ImageView)findViewById(R.id.main1);
        ImageView mn2=(ImageView)findViewById(R.id.main2);
        ImageView mn3=(ImageView)findViewById(R.id.main3);
        ImageView mn4=(ImageView)findViewById(R.id.main4);

        mn1.setBackgroundResource(R.drawable.bg);
        mn2.setBackgroundResource(R.drawable.olaf);
        mn3.setBackgroundResource(R.drawable.flower);
        mn4.setBackgroundResource(R.drawable.lala_land);
        mn1.setVisibility(View.INVISIBLE);
        mn2.setVisibility(View.INVISIBLE);
        mn3.setVisibility(View.INVISIBLE);
        mn4.setVisibility(View.INVISIBLE);

        btn_logOut = (Button)findViewById(R.id.logOut);
        mAuth = FirebaseAuth.getInstance();

        btn_logOut.setOnClickListener(this);

        mRef=new Firebase("https://appproject-khi.firebaseio.com/");

        mListView=(ListView)findViewById(R.id.main_listview);
        mListView.setOnItemClickListener(this);

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ids);
        mListView.setAdapter(arrayAdapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String k=dataSnapshot.child("계획된_시간").getValue(String.class);
                try {
                    if (k != null &&  !(k.equals("#")) ) {
                        value = dataSnapshot.child("타이머_시작시간").getValue(String.class);
                        ids.add(value);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                catch (NullPointerException e) {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        ImageView aa=(ImageView)findViewById(R.id.timer_icon_start);
        aa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), study_startbutton.class);
                Window w=getLocalActivityManager().startActivity("1", i);
                setContentView(w.getDecorView());
                //startActivity(i);
            }
        });
        ImageView aa2=(ImageView)findViewById(R.id.setting_icon);
        aa2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Background_settings_.class);
                //Window w=getLocalActivityManager().startActivity("1", i);
                //setContentView(w.getDecorView());
                startActivity(i);
            }
        });

        TextView bb=(TextView)findViewById(R.id.level_grade);
        bb.setText("level 1"); //default

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Toast myToast=Toast.makeText(study_record_list.this, "study hour : "+num +"s", Toast.LENGTH_SHORT);
                //View vv=getLayoutInflater().inflate(R.layout.toast, null);
                //myToast.setView(vv);
                myToast.show();
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num= 0;
                try {
                    Iterator<DataSnapshot> d=dataSnapshot.getChildren().iterator();
                    while(d.hasNext()) {
                        DataSnapshot dd = d.next();
                        String k = dd.child("계획된_시간").getValue(String.class);
                        try {
                            if (k != null && !(k.equals("#"))) {
                                String ss = dd.child("공부량").getValue(String.class);
                                if (ss != null) {
                                    num += Integer.parseInt(ss);
                                }
                            }
                        } catch (NullPointerException e) {

                        }
                    }

                    if(num.intValue() <=30) {  //여기서 공부량에 따라 레벨이 상승되도록 할 수 있음.
                        bb.setText("level 1");
                    }
                    else if(num.intValue() >30) {
                        bb.setText("level 2");
                    }
                }
                catch (NumberFormatException e) {
                    num=0;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //이미지 인덱스 불러오기
        Firebase mRef00=new Firebase("https://app2-7ca9a.firebaseio.com/");
        Firebase mRefChild00= mRef00.child("background_setting_index");

        mRefChild00.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String bg_num0 = dataSnapshot.getValue(String.class);
                    Integer bg_num = Integer.parseInt(bg_num0);
                    if (bg_num == 1) {
                        mn1.setVisibility(View.VISIBLE);
                        mn2.setVisibility(View.INVISIBLE);
                        mn3.setVisibility(View.INVISIBLE);
                        mn4.setVisibility(View.INVISIBLE);
                    } else if (bg_num == 2) {
                        mn1.setVisibility(View.INVISIBLE);
                        mn2.setVisibility(View.VISIBLE);
                        mn3.setVisibility(View.INVISIBLE);
                        mn4.setVisibility(View.INVISIBLE);
                    }
                    else if(bg_num==3) {
                        mn1.setVisibility(View.INVISIBLE);
                        mn2.setVisibility(View.INVISIBLE);
                        mn3.setVisibility(View.VISIBLE);
                        mn4.setVisibility(View.INVISIBLE);
                    }
                    else if(bg_num==4) {
                        mn1.setVisibility(View.INVISIBLE);
                        mn2.setVisibility(View.INVISIBLE);
                        mn3.setVisibility(View.INVISIBLE);
                        mn4.setVisibility(View.VISIBLE);
                    }
                }
                catch (NumberFormatException e) {}
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView=(TextView)view;
        category=textView.getText().toString();

        Intent intent=new Intent(view.getContext(), specific_list.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("MyEmail", account_email);
        savedInstanceState.putString("MyPassword", account_nick);
        savedInstanceState.putString("Join_Date", account_join);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        account_email = savedInstanceState.getString("MyEmail");
        account_nick = savedInstanceState.getString("MyPassword");
        account_join = savedInstanceState.getString("Join_Date");
        w.setText(account_email);
        ww.setText(account_nick);
        www.setText(account_join);
    }
}