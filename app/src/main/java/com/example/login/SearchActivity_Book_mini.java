package com.example.login;

public class SearchActivity_Book_mini {
    private String title;
    private String image;
    private String subject;

    public SearchActivity_Book_mini(String title, String image, String subject) {
        this.title = title;
        this.image = image;
        this.subject = subject;
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

    public String getSubject(){ return subject; }
    public void setSubject(String subject){
        this.subject=subject;
    }

    @Override
    public String toString() {
        return "Book [title=" + title +
                ", image=" + image +
                ", subject=" + subject +
                "]";
    }


}

