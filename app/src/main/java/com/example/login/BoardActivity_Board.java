package com.example.login;

import android.util.Log;

public class BoardActivity_Board {

    private String title;
    private String contents;
    private String name;
    private String date;


    public BoardActivity_Board(String title, String contents, String name, String date) {

        Log.d("★", "게시판 만들기 시작 ");
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.date = date;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }

    public String getContents(){
        return contents;
    }
    public void setContents(String contents){
        this.contents=contents;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){ this.date=date;}

    @Override
    public String toString() {
        return "BoardActivity_Board{" +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
