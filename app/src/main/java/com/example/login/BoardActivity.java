package com.example.login;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardActivity extends ActivityGroup implements View.OnClickListener {

    public static BoardActivity Board;
    private ArrayList<View> history; //intent within tablayout

    private Spinner spinner;
    public ArrayList<String> arrayList;
    public ArrayList<String> current;
    public ArrayList<Long> arrayListNum;
    public ArrayAdapter<String> arrayAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView myMainRecyclerView;
    private BoardAdapter myAdapter;
    private List<BoardActivity_Board> myBoardList;

    private String favoriteBoard;
    private String currentBoard;

    private String email;

    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }
        arrayList =new ArrayList<String>();
        Log.i("★", "유저 이메일 : "+email);
        //TextView debug =(TextView) findViewById(R.id.debug);
        Intent intent1 = getIntent();
        String selected;
        selected=intent1.getStringExtra("currentBoard");
        Log.i("★", "intent 값 : "+selected);
        if (selected!=null) {
            currentBoard=selected;
            Log.d("★", "현재 게시판 : "+currentBoard);
            arrayList.add(selected);
            //debug.setText(currentBoard);
            db.collection("board")
                    .document("root")
                    .collection(currentBoard)
                    .orderBy("date", Query.Direction.DESCENDING)//내림차순
                    .get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult() != null) {
                                for (DocumentSnapshot snap : task.getResult()) {
                                    Map<String, Object> shot = snap.getData();
                                    String title = (String) snap.getData().get("title");
                                    String content = (String) snap.getData().get("content");
                                    String name = (String) snap.getData().get("name");
                                    String date = (String) snap.getData().get("date");
                                    BoardActivity_Board data = new BoardActivity_Board(title, content, name, date);
                                    myBoardList.add(data);
                                    //Toast.makeText(BoardActivity.this, "불러오기 성공!", Toast.LENGTH_SHORT).show();
                                }
                                myAdapter = new BoardAdapter(myBoardList);
                                myMainRecyclerView.setAdapter(myAdapter);
                            }
                        }

                    });
        }
        

        else {
            Log.i("★", "널이라서 들어왔다 : "+selected);
            db.collection("regist_boards")
                    .document(email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            long currentvalue=0;
                            if (task.isSuccessful()) {;
                                if (task.getResult().exists()) {
                                    Map<String, Object> map = task.getResult().getData();
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        arrayList.add(entry.getKey());
                                        if ((Long)entry.getValue()>currentvalue){
                                            currentvalue= (Long) entry.getValue();
                                            currentBoard=entry.getKey();
                                        }
                                    }
                                }
                            }

                        }

                    });

        }

        if (currentBoard!=null) {
            /*db.collection("board")
                    .document("root")
                    .collection(currentBoard)
                    .orderBy("date", Query.Direction.DESCENDING)//내림차순
                    .get()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult() != null) {
                                for (DocumentSnapshot snap : task.getResult()) {
                                    Map<String, Object> shot = snap.getData();
                                    String title = (String) snap.getData().get("title");
                                    String content = (String) snap.getData().get("content");
                                    String name = (String) snap.getData().get("name");
                                    String date = (String) snap.getData().get("date");
                                    BoardActivity_Board data = new BoardActivity_Board(title, content, name, date);
                                    myBoardList.add(data);
                                    //Toast.makeText(BoardActivity.this, "불러오기 성공!", Toast.LENGTH_SHORT).show();
                                }
                                myAdapter = new BoardAdapter(myBoardList);
                                myMainRecyclerView.setAdapter(myAdapter);
                            }
                        }

                    });*/
        }



        //배경 세팅.

        myMainRecyclerView = findViewById(R.id.main_recycler_view);
        myBoardList = new ArrayList<>();
        ImageView mn1 = (ImageView) findViewById(R.id.main1);
        ImageView mn2 = (ImageView) findViewById(R.id.main2);
        ImageView mn3 = (ImageView) findViewById(R.id.main3);
        ImageView mn4 = (ImageView) findViewById(R.id.main4);

        mn1.setBackgroundResource(R.drawable.bg);
        mn2.setBackgroundResource(R.drawable.olaf);
        mn3.setBackgroundResource(R.drawable.flower);
        mn4.setBackgroundResource(R.drawable.lala_land);
        mn1.setVisibility(View.INVISIBLE);
        mn2.setVisibility(View.INVISIBLE);
        mn3.setVisibility(View.INVISIBLE);
        mn4.setVisibility(View.INVISIBLE);

        Firebase mRef00 = new Firebase("https://app2-7ca9a.firebaseio.com/");
        Firebase mRefChild00 = mRef00.child("background_setting_index");

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
                    } else if (bg_num == 3) {
                        mn1.setVisibility(View.INVISIBLE);
                        mn2.setVisibility(View.INVISIBLE);
                        mn3.setVisibility(View.VISIBLE);
                        mn4.setVisibility(View.INVISIBLE);
                    } else if (bg_num == 4) {
                        mn1.setVisibility(View.INVISIBLE);
                        mn2.setVisibility(View.INVISIBLE);
                        mn3.setVisibility(View.INVISIBLE);
                        mn4.setVisibility(View.VISIBLE);
                    }
                } catch (NumberFormatException e) {
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //배경 세팅 여기까지.
        history = new ArrayList<View>();
        Board = this;








        arrayAdapter = new ArrayAdapter<> (getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner = (Spinner) findViewById(R.id.board_spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("★", "게시판 이동 시작! ");
                if (!(currentBoard.equals(arrayList.get(position)))) {

                    Log.d("★", "현재 게시판 : "+currentBoard);
                    Log.d("★", "바꾸는 게시판 : "+arrayList.get(position));
                    currentBoard=arrayList.get(position);
                    db = FirebaseFirestore.getInstance();
                    db.collection("regist_boards")
                            .document(email)
                            .update(arrayList.get(position), FieldValue.increment(1));
                    Intent intent = new Intent(BoardActivity.this, BoardActivity.class);
                    finish();
                    intent.putExtra("currentBoard", arrayList.get(position));
                    Window window = getLocalActivityManager().startActivity("refresh", intent);
                    setContentView(window.getDecorView());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.main_write_button).setOnClickListener(this);
    }

    //new view
    public void replaceView(View view) {
        Log.d("★", "리플레이스 진입 ");
        //Toast.makeText(BoardActivity.this, "리플레이스 진입", Toast.LENGTH_SHORT).show();
        history.add(view);
        setContentView(view);
    }

    //back key
    public void back() {
        Log.d("★", "진입 ");
        //Toast.makeText(BoardActivity.this, "진입!", Toast.LENGTH_SHORT).show();
        if (history.size() > 0) {
            Log.d("★", "사이즈 큼 ");
            //Toast.makeText(BoardActivity.this, "사이즈큼!", Toast.LENGTH_SHORT).show();
            history.remove(history.size() - 1);
            if (history.size() == 0) {

                Log.d("★", "사이즈 0 ");
                //Toast.makeText(BoardActivity.this, "사이즈 0", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoardActivity.this, BoardActivity.class);
                intent.putExtra("currentBoard", currentBoard);
                View view = getLocalActivityManager().startActivity("BoardActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                replaceView(view);
                //finish();
            }
            else
                setContentView(history.get(history.size() - 1));
        } else {

            Log.d("★", "사이즈 없음 ");
            //Toast.makeText(BoardActivity.this, "사이즈없다!", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    @Override
    public void onBackPressed() {
        Board.back();
    }



    private class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.MainViewHolder>{

        private List<BoardActivity_Board> myBoardList;

        public BoardAdapter(List<BoardActivity_Board> myBoardList) {
            this.myBoardList=myBoardList;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_board_item_main, parent, false);
            MainViewHolder holder = new MainViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            BoardActivity_Board data=myBoardList.get(position);
            holder.myTitleTextView.setText(data.getTitle());
            holder.myNameTextView.setText(data.getName());
            holder.myDateTextView.setText(data.getDate());

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                    Intent intent = new Intent(BoardActivity.this, BoardActivity_Read.class);
                    intent.putExtra("boardtitle", data.getTitle());
                    intent.putExtra("currentBoard", currentBoard);
                    View view = getLocalActivityManager().startActivity("BoardActivity_Read", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                    replaceView(view);
                }
            });
        }

        @Override
        public int getItemCount() {
            return myBoardList.size();
        }

        class MainViewHolder extends RecyclerView.ViewHolder{
            private TextView myTitleTextView;
            private TextView myNameTextView;
            private TextView myDateTextView;

            public MainViewHolder (View itemView){
                super(itemView);
                myTitleTextView=itemView.findViewById(R.id.item_title_text);
                myNameTextView=itemView.findViewById(R.id.item_name_text);
                myDateTextView=itemView.findViewById(R.id.item_date_text);

            }
        }
    }/**/
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(BoardActivity.this, BoardActivity_Write.class);
        intent.putExtra("currentBoard", currentBoard);
        View view = getLocalActivityManager().startActivity("BoardActivity_Write", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
        replaceView(view);
    }
}