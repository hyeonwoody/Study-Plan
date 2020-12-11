package com.example.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityGroup;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchActivity extends ActivityGroup implements View.OnClickListener{
    private static String[] NUMBERS=new String[] {"책이름 검색", "과목별 검색","ISBN 검색"};

    private Button title;

    private RelativeLayout map_title;
    private RelativeLayout map_isbn;

    private Button subject;
    private Button isbn;

    private TextView textView;
    private SearchView searchView;
    private SearchView searchView1;

    public static SearchActivity Search;
    private ArrayList<View> history; //intent within tablayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //textView=findViewById(R.id.text_view);

        title=(Button)findViewById(R.id.title);
        title.setText(NUMBERS[0]);
        title.setTypeface(getResources().getFont(R.font.timon));
        title.setTextSize(40);
        title.setTextColor(Color.BLACK);
        title.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient_yellow));
        title.setOnClickListener(this);

        map_title=(RelativeLayout)findViewById(R.id.map_title);
        map_title.setVisibility(View.GONE);

        map_isbn=(RelativeLayout)findViewById(R.id.map_isbn);
        map_isbn.setVisibility(View.GONE);

        subject=(Button)findViewById(R.id.subject);
        subject.setText(NUMBERS[1]);
        subject.setTypeface(getResources().getFont(R.font.timon));
        subject.setTextSize(40);
        subject.setTextColor(Color.BLACK);
        subject.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient_orange));
        subject.setOnClickListener(this);

        isbn=(Button)findViewById(R.id.ISBN);
        isbn.setText(NUMBERS[2]);
        isbn.setTypeface(getResources().getFont(R.font.timon));
        isbn.setTextSize(40);
        isbn.setTextColor(Color.BLACK);
        isbn.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient_red));
        isbn.setOnClickListener(this);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("책 이름을 입력해주세요");
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryRefinementEnabled(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(queryTextListener);

        SearchManager searchManager1 = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView1 = findViewById(R.id.search_view1);
        searchView1.setSearchableInfo(searchManager1.getSearchableInfo(getComponentName()));
        searchView1.setIconifiedByDefault(true);
        searchView1.setQueryHint("ISBN 번호를 입력해주세요");
        searchView1.setSubmitButtonEnabled(true);
        searchView1.setQueryRefinementEnabled(true);
        searchView1.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView1.setOnQueryTextListener(queryTextListener1);

        history = new ArrayList<View>();
        Search = this;

        /*FirebaseFirestore bookdb=FirebaseFirestore.getInstance();
        String title = "두번째 테스트용 책";
        Map<String, Object> user = new HashMap<>();
        user.put("author", "정현우");
        user.put("image", "http://bimage.interpark.com/goods_image/0/4/6/4/333290464g.jpg");
        user.put("isbn", "131");
        user.put("price", "공짜");
        user.put("students", 0);
        user.put("page", 1357);
        user.put("subject", "국어");
        user.put("title", title);
        user.put("top", "없음");

        bookdb.collection("book")
                .document(title)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("★", "업로드함 : ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent = new Intent(SearchActivity.this, SearchActivity_able.class);
            intent.putExtra("query", query);
            View view = getLocalActivityManager().startActivity("query to searhcable", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            title.setText(NUMBERS[0]);
            //searchView.clearFocus();
            replaceView(view);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            title.setText(newText);
            Log.i("★", "검색 단어 입력중.."+newText);
            return false;
        }
    };

    private SearchView.OnQueryTextListener queryTextListener1 = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent = new Intent(SearchActivity.this, SearchActivity_result.class);
            intent.putExtra("isbn", query);
            Log.i("★", "제출 단어 : "+query);
            View view = getLocalActivityManager().startActivity("isbn to result", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            title.setText(NUMBERS[0]);
            replaceView(view);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    //new view
    public void replaceView(View view) {
        Log.d("★", "리플레이스뷰 진입 ");
        //Toast.makeText(SearchActivity.this, "리플레이스 진입", Toast.LENGTH_SHORT).show();
        history.add(view);
        setContentView(view);
    }

    //back key
    public void back() {
        Log.d("★", "검색 ");
        //Toast.makeText(SearchActivity.this,  "서치!", Toast.LENGTH_SHORT).show();
        if (history.size() > 0) {
            Log.d("★", "사이즈 큼");
            //Toast.makeText(SearchActivity.this, "사이즈빅!", Toast.LENGTH_SHORT).show();
            history.remove(history.size() - 1);
            if (history.size() == 0) {
                Log.d("★", "사이즈 0 ");
                //Toast.makeText(SearchActivity.this, "size 0", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                View view = getLocalActivityManager().startActivity("SearchActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                replaceView(view);
                //finish();
            }
            else
                setContentView(history.get(history.size() - 1));
        } else {
            Log.d("★", "사이즈 없음 ");
            //Toast.makeText(SearchActivity.this, "사이즈 none!", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("★", "뒤로가기");
        Search.back();
    }
    
    /*@Override
    protected  void onNewIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query=intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }


    void search(String query){
        String[] books=getResources().getStringArray(R.array.books);
        for (String book : books){
            if (book.toLowerCase().contains(query.toLowerCase())){
                textView.setText(book);
            }
            else {
                textView.setText(query);
            }
        }
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doSearch(query);
        }
    }*/
    @Override
    public void onClick(View v) {
        int visibility;
        int visibility1;
        switch (v.getId()) {

            case R.id.title :

                visibility=map_title.getVisibility();
                if (visibility==View.GONE) {
                    Log.i("★", "책 제목 검색 하기");
                    map_title.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    map_title.setVisibility(View.VISIBLE);
                                }
                            });
                }
                else {
                    Log.i("★", "책 제목 검색 닫기");
                    map_title.animate()
                            .translationY(-map_title.getHeight())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    map_title.setVisibility(View.GONE);
                                }
                            });
                    title.setText(NUMBERS[0]);
                }
                break;
            case R.id.subject:
                visibility=map_title.getVisibility();
                visibility1=map_isbn.getVisibility();
                if (visibility==View.GONE && visibility1==View.GONE){
                }
                else {
                    if (visibility == View.VISIBLE) {
                        Log.i("★", "책 입력 검색 가리기");
                        map_title.animate()
                                .translationY(-map_title.getHeight())
                                .alpha(0.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        map_title.setVisibility(View.GONE);
                                    }
                                });
                        title.setText(NUMBERS[0]);
                    }
                    if (visibility1 == View.VISIBLE) {
                        Log.i("★", "isbn 입력 검색 가리기");
                        map_isbn.animate()
                                .translationY(+map_isbn.getHeight())
                                .alpha(0.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        map_isbn.setVisibility(View.GONE);
                                    }
                                });
                    }
                }
                break;
            case R.id.ISBN:
                visibility1=map_isbn.getVisibility();
                if (visibility1==View.GONE) {
                    Log.i("★", "isbn 검색");
                    map_isbn.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    map_isbn.setVisibility(View.VISIBLE);
                                }
                            });
                }
                else {
                    Log.i("★", "isbn 가리기");
                    map_isbn.animate()
                            .translationY(+map_isbn.getHeight())
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    map_isbn.setVisibility(View.GONE);
                                }
                            });
                }
                break;
            /*case R.id.start_search:
                Intent intent =getIntent();
                String query=intent.getStringExtra(SearchManager.QUERY);*/



            default : break;
        }

    }
}
