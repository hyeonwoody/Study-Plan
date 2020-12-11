package com.example.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityGroup;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.lang.Object;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity_result extends ActivityGroup implements View.OnClickListener {

    private TextView debug;

    private ImageView bookimage;
    private TextView booktitle;
    private TextView bookauthor;
    private TextView bookisbn;
    private TextView bookpage;
    private TextView booksubject;
    private TextView bookstudents;
    private TextView booktop;
    private TextView bookprice;

    private Button backButton;
    private Button registerButton;
    private Button deleteButton;


    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseFirestore db1;
    private String email;
    
    private String isbn;
    private String title;

    public static SearchActivity_result Search_result;
    private SearchActivity_Book book;
    
    private Array datalist;

    private String path;

    private boolean initialized=false;
    private boolean registered=false;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        debug=(TextView)findViewById(R.id.notfound);

        backButton=(Button) findViewById(R.id.back);
        registerButton=(Button) findViewById(R.id.register);
        deleteButton=(Button) findViewById(R.id.delete);

        registerButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        Search_result =this ;

        book= new SearchActivity_Book(null,  null, null, 0, null, null, 0, null, null);
        Intent intent = getIntent();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }


        if (intent.getStringExtra("bookname")!=null){
            title=intent.getStringExtra("bookname");
            db = FirebaseFirestore.getInstance();
            db.collection("regist_books").document(email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snap = task.getResult();
                                if (snap.exists()) {
                                    registered = snap.contains(title);
                                    if (registered) {
                                        registerButton.setVisibility(View.GONE);
                                        deleteButton.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        registerButton.setVisibility(View.VISIBLE);
                                        deleteButton.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    });
            getbookinfobytitle();

        }
        if (intent.getStringExtra("isbn")!=null){
            isbn=intent.getStringExtra("isbn");
            getbookinfobyisbn();

        }


        backButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private void print(boolean i) throws IOException {
        if (i == true) {
            debug.setVisibility(View.GONE);
            booktop=(TextView)findViewById(R.id.book_top);
            /**/bookimage=(ImageView)findViewById(R.id.book_image);
            booktitle=(TextView)findViewById(R.id.book_name);
            bookauthor=(TextView)findViewById(R.id.book_author);
            bookisbn=(TextView)findViewById(R.id.book_isbn);
            bookpage=(TextView)findViewById(R.id.book_page);
            bookprice = (TextView)findViewById(R.id.book_price);
            booksubject=(TextView)findViewById(R.id.book_subject);
            bookstudents=(TextView)findViewById(R.id.book_students);

            debug.setText("프린트시작 : " + book.getTitle());

            booktitle.setText("책 제목 : "+book.getTitle());
            bookauthor.setText("출판사 : "+book.getAuthor());
            bookprice.setText("가격 : "+book.getPrice());
            bookisbn.setText("ISBN : "+book.getISBN());
            booksubject.setText("과목 : "+book.getSubject());
            bookstudents.setText("같이 공부하는 학생 : "+Long.toString(book.getStudents())+"명");
            booktop.setText("가장 많이 공부한 학생 : 없음");

            URL url = new URL (book.getImage());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            bookimage.setImageBitmap(bmp);
            Glide.with(this)
                    .load("http://goo.gl/gEgYUd")
                    .override(3000,2000)
                    .dontAnimate()
                    .into(bookimage);
        }
        else {}
    }




    private void getbookinfobytitle() {
        
        db = FirebaseFirestore.getInstance();
        //DocumentReference docRef=db.collection("book").document(title);
        db.collection("book")
                .document(title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        initialized =false;
                        if (task.isSuccessful()) {
                            DocumentSnapshot snap = task.getResult();
                            String image = (String) snap.getData().get("image");
                            String subject = (String) snap.getData().get("subject");
                            String author = (String) snap.getData().get("author");
                            long page = (long) snap.getData().get("page");
                            String price = (String) snap.getData().get("price");
                            long students = (long) snap.getData().get("students");
                            String isbn = (String) snap.getData().get("isbn");
                            String top = (String) snap.getData().get("top");
                            Log.i("★", title+"결과");
                            book = new SearchActivity_Book(title, image, author, page, price, isbn, students, subject, top);
                            initialized = true;
                        }
                        try {
                            print (initialized);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    
                });
    }


    private void getbookinfobyisbn() {
        db = FirebaseFirestore.getInstance();
        db1 = FirebaseFirestore.getInstance();
        db.collection("book")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (task.getResult() != null) {
                            for (DocumentSnapshot snap : task.getResult()) {
                                String dataisbn = (String) snap.getData().get("isbn");

                                if (dataisbn.equals(isbn)) {
                                    
                                    String image = (String) snap.getData().get("image");
                                    String subject = (String) snap.getData().get("subject");
                                    String author = (String) snap.getData().get("author");
                                    long page = (long) snap.getData().get("page");
                                    String price = (String) snap.getData().get("price");
                                    long students = (long) snap.getData().get("students");
                                    String title = (String) snap.getData().get("title");
                                    String top = (String) snap.getData().get("top");
                                    book = new SearchActivity_Book(title, image, author, page, price, isbn, students, subject, top);
                                    Log.i("★", title+"결과");
                                    initialized = true;
                                    if (book.getTitle()!=null) {
                                        db1 = FirebaseFirestore.getInstance();
                                        db1.collection("regist_books").document(user.getEmail())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot snap = task.getResult();
                                                            if (snap.exists()) {
                                                                registered = snap.contains(isbn);
                                                                if (registered) {
                                                                    registerButton.setVisibility(View.GONE);
                                                                    deleteButton.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    registerButton.setVisibility(View.VISIBLE);
                                                                    deleteButton.setVisibility(View.GONE);
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                    break;
                                }
                            }
                        }
                        try {
                            print (initialized);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });


    }



    private void checkbookname() {

    }

    /*private boolean alreadyregistered() {
        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            String email=user.getEmail();
        }
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null) {
                            for (DocumentSnapshot snap : task.getResult()) {
                                String data = (String) snap.getData().get("email");
                                if (data.equals(email.toLowerCase())) {
                                    datalist = (List) snap.getData().get("regist_books");
                                    break;
                                }
                            }
                        }
                    }
                });
    }*/


    @Override
    public void onBackPressed(){
        back();
        
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back :
                back();
                break;
            case R.id.register:
                db = FirebaseFirestore.getInstance();
                Map<String, Object> post = new HashMap<>();
                post.put(book.getTitle(), 1);
                post.put(book.getISBN(), 1);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                db.collection("regist_books")
                        .document(user.getEmail())
                        .update(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("★", "update3게시판 등록 성공 : ");
                                db.collection("book")
                                        .document(book.getTitle())
                                        .update("students", FieldValue.increment(1));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("★", "update 2게시판 등록 실패 : ");
                            }
                        });
                registerButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.VISIBLE);

                db1 = FirebaseFirestore.getInstance();
                Map<String, Object> post1 = new HashMap<>();
                post1.put("정오표 : "+book.getTitle(), 1);
                post1.put("Q & A : "+book.getTitle(), 1);
                post1.put("성적 게시판 : "+book.getTitle(), 1);
                post1.put("공부시간 : "+book.getTitle(), 1);
                Log.i("★", "사용자 이메일 : "+user.getEmail());
                db1.collection("regist_boards")
                        .document(user.getEmail())
                        .set(post1)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("★", " update1 게시판 등록 실패 : ");
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i("★", "update0게시판 등록 성공 : ");

                            }
                        });
                break;
            case R.id.delete :
                db = FirebaseFirestore.getInstance();

                user = FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference ref = db.collection("regist_books").document(user.getEmail());
                Map<String, Object> updates = new HashMap<>();
                updates.put(book.getTitle(), FieldValue.delete());
                updates.put(book.getISBN(), FieldValue.delete());


                ref.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("★", "지우기 성공 : ");
                        db.collection("book")
                                .document(book.getTitle())
                                .update("students", FieldValue.increment(-1));
                    }
                });
                deleteButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);

                ref = db.collection("regist_boards").document(user.getEmail());
                updates.put("정오표 : "+book.getTitle(), FieldValue.delete());
                updates.put("Q & A : "+book.getTitle(), FieldValue.delete());
                updates.put("성적 게시판 : "+book.getTitle(), FieldValue.delete());
                updates.put("공부시간 : "+book.getTitle(), FieldValue.delete());
                ref.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("★", "지우기 성공 : ");
                    }
                });
                break;
            default : break;

        }
    }

    private void back() {
        Intent intent = new Intent(SearchActivity_result.this, SearchActivity.class);
        Window window = getLocalActivityManager().startActivity("to Search", intent);
        setContentView(window.getDecorView());
    }



}