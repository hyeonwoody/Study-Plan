package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlertCall extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
//    String [] category;
    String [] value=new String[8];
    String [] tel=new String[8];
    private ArrayList<String> ids=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_call);
        mListView=(ListView)findViewById(R.id.main_listview_2);
        mListView.setOnItemClickListener(this);
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ids);
        mListView.setAdapter(arrayAdapter);

        value[0]="경찰서(112)";
        value[1]="화재구급구조(119)";
        value[2]="해양재난신고(122)";
        value[3]="간첩신고(113)";
        value[4]="필수신고(125)";
        value[5]="국정원(111)";
        value[6]="학교폭력 신고 및 상담(117)";
        value[7]="사이버테러신고(118)";
        for(int i=0;i<8;i++) {
            ids.add(value[i]);
        }
        arrayAdapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        TextView textView=(TextView)view;
//        category[0]=textView.getText().toString();
//        Intent intent=new Intent(view.getContext(), specific_list.class);
//        category[1]=textView.getText().toString();

        switch (i) {
            case 0:
                Intent tt0 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 112));
                startActivity(tt0);
                break;
            case 1:
                Intent tt1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 119));
                startActivity(tt1);
                break;
            case 2:
                Intent tt2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 122));
                startActivity(tt2);
                break;
            case 3:
                Intent tt3 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 113));
                startActivity(tt3);
                break;
            case 4:
                Intent tt4 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 125));
                startActivity(tt4);
                break;
            case 5:
                Intent tt5 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 111));
                startActivity(tt5);
                break;
            case 6:
                Intent tt6 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 127));
                startActivity(tt6);
                break;
            case 7:
                Intent tt7 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 118));
                startActivity(tt7);
                break;
        }
//        intent.putExtra("category", category);
//        startActivity(intent);
    }
}