package com.example.login;



import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity_able extends ActivityGroup {

    public static SearchActivity_able Search_able;
    private ArrayList<View> history; //intent within tablayout

    private String keyword;

    private RecyclerView bookSearchView;
    private SearchAdapter Adapter;
    private List<SearchActivity_Book_mini> mySearchList;

    private FirebaseFirestore bookdb = FirebaseFirestore.getInstance();
    private String subject;

    private TextView result;

    private static final String TAG = "★☆★☆Search_able☆★☆★";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_able);
        bookSearchView=findViewById(R.id.search_recycler_view);

        mySearchList = new ArrayList<>();
        Intent intent = getIntent();
        keyword = intent.getStringExtra("query");

        result = (TextView) findViewById(R.id.notfound);
        //result.setVisibility(View.VISBLe);

        bookdb.collection("book")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult() != null) {
                            for (DocumentSnapshot snap : task.getResult()) {
                                String title = (String) snap.getData().get("title");
                                if (title.toLowerCase().contains(keyword.toLowerCase())) {
                                    Log.i("★", "검색 단어 : "+keyword);
                                    String image = (String) snap.getData().get("image");
                                    subject =(String) snap.getData().get("subject");

                                    SearchActivity_Book_mini data = new SearchActivity_Book_mini (title, image, subject);
                                    mySearchList.add(data);
                                }
                            }
                            Adapter = new SearchAdapter(mySearchList);
                            bookSearchView.setAdapter(Adapter);
                        }
                    }
                });


    }
    //new view
    public void replaceView(View view) {
        //Toast.makeText(BoardActivity.this, "리플레이스 진입", Toast.LENGTH_SHORT).show();
        history.add(view);
        setContentView(view);
    }

    //back key
    public void back() {
        //Toast.makeText(BoardActivity.this, "진입!", Toast.LENGTH_SHORT).show();
        if (history.size() > 0) {
            //Toast.makeText(BoardActivity.this, "사이즈큼!", Toast.LENGTH_SHORT).show();
            history.remove(history.size() - 1);
            if (history.size() == 0) {
                //Toast.makeText(BoardActivity.this, "사이즈 0", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity_able.this, SearchActivity_able.class);
                View view = getLocalActivityManager().startActivity("BoardActivity", intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
                replaceView(view);
                //finish();
            }
            else
                setContentView(history.get(history.size() - 1));
        } else {
            //Toast.makeText(BoardActivity.this, "사이즈없다!", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    @Override
    public void onBackPressed() {
        Search_able.back();
    }
    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MainViewHolder>{

        private List<SearchActivity_Book_mini> mySearchList;

        public SearchAdapter(List<SearchActivity_Book_mini> mySearchList) {
            this.mySearchList=mySearchList;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_item_book, parent, false);
            MainViewHolder holder = new MainViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            SearchActivity_Book_mini data=mySearchList.get(position);
            result.setVisibility(View.GONE);
            //Toast.makeText(SearchActivity_able.this, data.getSubject(), Toast.LENGTH_SHORT).show();;
            switch(data.getSubject()) {
                case "국어":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_red));
                    break;
                case "영어":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_orange));
                    break;
                case "수학 가형":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_green));
                    break;
                case "수학 나형":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_green));
                    break;
                case "한국사":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_deeporange));
                    break;
                case "생활과 윤리": case "윤리와 사상": case "한국 지리": case "세계 지리": case "동아시아사": case "세계사": case "법과 정치": case "경제":
                case "사회 문화":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_purple));
                    break;
                case "물리1": case "화학1": case "생명과학1": case "지구과학1": case "물리2": case "화학2": case "생명과학2":
                case "지구과학2":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_blue));
                    break;
                case "농업 이해": case "농업 기초 기술": case "공업 일반": case "기초 제도": case "상업 경제": case "해양의 이해": case "수산 해운 정보 처리":
                case "인간 발달":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_yellow));
                    break;
                case "독일어": case "프랑스어": case "스페인어": case "중국어": case "일본어": case "러시아어": case "아랍어": case "베트남어":
                case "한문":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_sky));
                    break;
                case "기타":
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_inyellow));
                    break;
                default:
                    holder.itemView.setBackground(ContextCompat.getDrawable(SearchActivity_able.this, R.drawable.gradient_default));
                    break;
            }
                holder.title.setText(data.getTitle());
                Glide.with(holder.bookcover.getContext())
                    .load(data.getImage())
                    .override(3000,2000)
                    .into(holder.bookcover);

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                    Intent intent = new Intent(SearchActivity_able.this, SearchActivity_result.class);
                    intent.putExtra("bookname", data.getTitle());
                    Log.i("★", data.getTitle()+"선택");
                    Window window = getLocalActivityManager().startActivity("to Search Result", intent);
                    setContentView(window.getDecorView());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mySearchList.size();
        }

        class MainViewHolder extends RecyclerView.ViewHolder{
            private TextView title;
            private ImageView bookcover;
            public MainViewHolder (View i){
                super(i);
                title=(TextView)i.findViewById(R.id.able_title);
                bookcover=(ImageView)i.findViewById(R.id.able_cover);

            }
        }
    }


}


