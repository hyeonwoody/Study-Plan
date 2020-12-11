package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TabHost;

import com.firebase.client.Firebase;

public class TabActivity extends android.app.TabActivity implements TabHost.OnTabChangeListener {
    TabHost tabHost;
    OnSwipeTouchListener onSwipeTouchListener;
    Integer tabsNum=4;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_bar);
        Firebase.setAndroidContext(this); //반드시 표시해야 함.


        tabHost=getTabHost();
        tabHost.setOnTabChangedListener(this);

        onSwipeTouchListener = new OnSwipeTouchListener() {
            /*public boolean onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                return true;
            }*/

            public boolean onSwipeLeft() {
                if(tabHost.getCurrentTab()<tabsNum-1){tabHost.setCurrentTab(tabHost.getCurrentTab()+1);}
                //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                return true;
            }
            public boolean onSwipeRight() {
                if(tabHost.getCurrentTab()>0){tabHost.setCurrentTab(tabHost.getCurrentTab()-1);}
                //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                return true;
            }
            /*public boolean onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                return true;
            }*/
        };

        tabHost.setOnTouchListener(onSwipeTouchListener);



        TabHost.TabSpec spec;
        Intent intent;

        intent=new Intent().setClass(this, study_record_list.class);
        spec=tabHost.newTabSpec("0").setIndicator("홈").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SearchActivity.class);
        spec = tabHost.newTabSpec("1").setIndicator("검색")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, BoardActivity.class);
        spec = tabHost.newTabSpec("2").setIndicator("게시판")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this,GradeActivity.class);
        spec = tabHost.newTabSpec("3").setIndicator("성적")
                .setContent(intent);
        tabHost.addTab(spec);


        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_menu_home);
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_menu_search);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_menu_board);
        tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.tab_menu_grade);
        tabHost.getTabWidget().setCurrentTab(0);
    }

    @Override
    public void onTabChanged(String tabId) {
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            if(i==0)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_menu_home);
            else if(i==1)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_menu_search);
            else if(i==2)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_menu_board);
            else
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_menu_grade);
        }
        if(tabHost.getCurrentTab()==0)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_menu_home);
        else if(tabHost.getCurrentTab()==1)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_menu_search);
        else if(tabHost.getCurrentTab()==2)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_menu_board);
        else
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_menu_grade);
    }

}