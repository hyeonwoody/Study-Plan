package com.example.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
public class SearchActivity_Book{
    private String title;
    private String image;
    private String author;
    private String price;
    private String isbn;
    private long page;
    private long students; //how many students study
    private String subject;
    private String top;

    private static int i=0;

    private SearchActivity_Book book;
     //SearchActivity_Book(title, image, author, page, price, isbn, students, subject, top);
    public SearchActivity_Book (String title, String image, String author, long page, String price, String isbn, long students, String subject, String top) {
        this.title = title;
        this.image = image;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        this.page = page;
        this.students = students;
        this.subject = subject;
        this.top = top;
        i++;
    }

    public boolean initialized() {
        if (i==1){
            i--;
            return false;
        }
        return true;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image=image;
    }

    public String getAuthor(){
        return author;
    }
    public void setAuthor(String publisher){
        this.author=author;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price=price;
    }

    public String getISBN(){
        return isbn;
    }
    public void setISBN(String isbn){
        this.isbn=isbn;
    }

    public long getPage(){
        return page;
    }
    public void setPage(int page){
        this.page=page;
    }

    public long getStudents(){
        return students;
    }
    public void setStudents(long students){
        this.students=students;
    }

    public String getSubject(){
        return subject;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }

    public void addStudent(){
        this.students = this.students + 1;
    }

    @Override
    public String toString() {
        return "Book [title=" + title +
                ", image=" + image +
                ", author=" + author +
                ", page=" + page +
                ", price=" + price +
                ", isbn=" + isbn +
                ", students=" + students +
                ", subject=" + subject +
                ", top=" + top +
                "]";
    }


}

